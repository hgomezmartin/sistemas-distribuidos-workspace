import os, time, mysql.connector, requests
from flask import Flask, jsonify, abort
from flask_cors import CORS

app = Flask(__name__)
CORS(app)    #permite peticiones desde el front

# ---------- util db ----------
def get_db_conn():
    return mysql.connector.connect(
        host=os.getenv("MYSQL_HOST", "mysqldb"),
        user=os.getenv("MYSQL_USER", "root"),
        password=os.getenv("MYSQL_PASSWORD", "123456"),
        database=os.getenv("MYSQL_DATABASE", "libreria"),
        ssl_disabled=True
    )

# ---------- 0) ping ----------
@app.get("/ping")
def ping():
    return jsonify({"status": "ok", "msg": "pong"})

# ---------- 1) Ficheros ----------
FORBIDDEN = {"confidential.txt"} #ficheros que establecemos como secretos

@app.get("/api/file/<path:filename>")
def read_file(filename):
    if filename in FORBIDDEN:
        abort(403, f"Permiso denegado para {filename}")
    try:
        with open(f"./files/{filename}") as f:
            return jsonify({"file": filename, "content": f.read()})
    except FileNotFoundError:
        abort(404, f"El archivo {filename} no existe")

# ---------- 2) BD: lista users ----------
@app.get("/api/db/users")
def list_users():
    conn = None #declaramos antes del try q si no nos da error de no declaracion
    try:
        conn = get_db_conn()
        cur  = conn.cursor(dictionary=True)
        cur.execute("SELECT id, username, email FROM users")
        return jsonify(cur.fetchall())
    except mysql.connector.Error as e:
        abort(500, f"Error de base de datos: {e}")
    finally:
        if conn is not None:
            conn.close()


# ---------- 2b) BD: fuerza error ----------
@app.get("/api/db/force-error") #forxamos el error seleccionando cosas de una tabla q no existe
def db_force_error():
    try:
        conn = get_db_conn()
        conn.cursor().execute("SELECT * FROM tabla_que_no_existe")
    except mysql.connector.Error as e:
        abort(500, f"Error forzado BD: {e}")
    finally:
        if conn: conn.close()

# ---------- 3) API externa (PokeAPI) ----------
@app.get("/api/pokemon/<name>") #hacemos llamadas de distintos pokemons a pokeapi
def get_pokemon(name):
    try:
        r = requests.get(f"https://pokeapi.co/api/v2/pokemon/{name}", timeout=4)
        r.raise_for_status()
        data = r.json()
        return jsonify({"name": data["name"], "height": data["height"]})
    except requests.exceptions.HTTPError:
        abort(r.status_code, f"PokeAPI devolvió {r.status_code}")
    except requests.exceptions.RequestException as e:
        abort(500, f"Error de red: {e}")

# ---------- 4) Timeout ----------
@app.get("/api/timeout")
def timeout():
    time.sleep(10)  #nuestro RestTemplate tiene 5s, esto provocara timeout en nuestro front, pero no en postman
    return jsonify({"msg": "tarde pero llegué"})

# ---------- manejador global ----------
@app.errorhandler(Exception)
def global_err(err):
    status = getattr(err, "code", 500)
    return jsonify({"error": str(err)}), status

# ---------- run ----------
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
