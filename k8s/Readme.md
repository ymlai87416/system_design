## K8s

Install the following service to be shared across within cluster. e.g. 

```
apiVersion: v1
kind: Service
metadata:
  name: my-service
  namespace: prod

refer as: service.ns.svc.cluster.local
```

### Services

helm chart: minio
link: https://github.com/minio/minio/tree/master/helm/minio

```bash
helm repo add minio https://charts.min.io/
helm install minio-release --namespace minio --set rootUser=minio,rootPassword=xxxxxx --generate-name minio/minio
```

helm chart: k8s-spark-operator
link: https://github.com/GoogleCloudPlatform/spark-on-k8s-operator

```bash
helm repo add spark-operator https://googlecloudplatform.github.io/spark-on-k8s-operator
helm install my-release spark-operator/spark-operator --namespace spark-operator --create-namespace
```

helm chart: zookeeper
link: https://github.com/bitnami/charts/tree/master/bitnami/zookeeper

```bash
$ helm repo add bitnami https://charts.bitnami.com/bitnami
$ helm install my-release bitnami/zookeeper
```


helm chart: kafka
link: https://github.com/bitnami/charts/tree/master/bitnami/kafka

```bash
helm install my-release bitnami/kafka
```
