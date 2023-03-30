import os

# Kafka
KAFKA_SERVER = os.environ.get('KAFKA_SERVER')
TOPIC = 'testapp-batch-finish'

# redis server
REDIS_SERVER=os.environ.get('REDIS_SERVER')
REDIS_PORT=os.environ.get('REDIS_PORT')

# notification server
NOT_SERVER=os.environ.get('NOT_SERVER')
