FROM python:3.8-slim

RUN mkdir /app
WORKDIR /app

COPY requirements.txt .
COPY config.py .
COPY consumer.py .

RUN pip install --no-cache-dir -r requirements.txt

ENTRYPOINT ["python","/app/consumer.py"]