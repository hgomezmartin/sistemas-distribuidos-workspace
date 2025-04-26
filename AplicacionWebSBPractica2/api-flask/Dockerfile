FROM python:3.12-slim
WORKDIR /app

# instala las dependencias
COPY requirements.txt .
RUN pip install -r requirements.txt

# copia el código
COPY app.py .
COPY files/ ./files/


EXPOSE 5000
CMD ["python", "app.py"]