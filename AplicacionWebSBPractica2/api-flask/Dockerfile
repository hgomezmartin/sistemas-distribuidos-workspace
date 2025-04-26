FROM python:3.12-slim
WORKDIR /app

# instala las dependencias
COPY requirements.txt .
RUN pip install -r requirements.txt

# copia el código y archivos de prueba
COPY app.py .
COPY files/ ./files/

#puerto expuesto para l contenedor y comando de arranque
EXPOSE 5000
CMD ["python", "app.py"]