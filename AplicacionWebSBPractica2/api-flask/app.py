from flask import Flask, jsonify, abort
app = Flask(__name__)

# --------End‑points de tercero---s-----------
@app.get("/ping")
def ping():
    return jsonify({"status": "ok", "msg": "pong"})

@app.get("/users")
def fake_users():
    data = [
        {"id": 1, "username": "peter"},
        {"id": 2, "username": "celio"},
    ]
    return jsonify(data)

# End‑point que provocará un 500 manual
@app.get("/boom")
def boom():
    abort(500, description="Intentional error from Flask service")
# --------------------------------------------


# --------API que lee MYSQL_DATABASE----------
def get_db_conn():
    return mysql.connector.connect(
        host=os.environ.get("MYSQL_HOST","mysqldb"),
        user=os.environ.get("MYSQL_USER","root"),
        password=os.environ.get("MYSQL_PASSWORD","123456"),
        database=os.environ.get("MYSQL_DATABASE","libreria")
    )

@app.get("/api/users")
def list_users():
    try:
        conn = get_db_conn()
        cur  = conn.cursor(dictionary=True)
        cur.execute("SELECT id, username, email FROM users")
        rows = cur.fetchall()
        return jsonify(rows)
    except mysql.connector.Error as e:
        # muestra el error pero no filtra la traza
        return jsonify({"error": str(e)}), 500
    finally:
        if conn: conn.close()

# --------------------------------------------

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
