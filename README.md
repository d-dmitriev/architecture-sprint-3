# Базовая настройка

## Запуск minikube

[Инструкция по установке](https://minikube.sigs.k8s.io/docs/start/)

```bash
minikube start
```

## Установка Istio

[Install Istio CLI](https://istio.io/latest/docs/ops/diagnostic-tools/istioctl)

```bash
istioctl install -f ./tracing.yaml -y
```

## Установка Kiali

```bash
kubectl apply -f ./kiali.yaml
```

## Установка Prometeus

```bash
kubectl apply -f ./prometheus.yaml
```

## Установка Grafana

```bash
kubectl apply -f ./grafana.yaml
```

## Установка Jaeger

```bash
kubectl apply -f ./jaeger.yaml
```

## Настройка terraform

[Установите Terraform](https://yandex.cloud/ru/docs/tutorials/infrastructure-management/terraform-quickstart#install-terraform)

Создайте файл ~/.terraformrc

```hcl
provider_installation {
  network_mirror {
    url = "https://terraform-mirror.yandexcloud.net/"
    include = ["registry.terraform.io/*/*"]
  }
  direct {
    exclude = ["registry.terraform.io/*/*"]
  }
}
```

## Применяем terraform конфигурацию

```bash
cd terraform
terraform apply
```

## Проверяем работоспособность

### Подключение к Keycloak

```bash
kubectl port-forward svc/keycloak -n keycloak  8081:80
```

### Получение токена для авторизации

```bash
curl --location 'http://localhost:8081/realms/master/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=admin-cli' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=admin' \
--data-urlencode 'scope=openid'
```

#### Ответ

```json
{"access_token":"TOKEN_TO_COPY","expires_in":60,"refresh_expires_in":1800,"refresh_token":"...","token_type":"Bearer","id_token":"...","not-before-policy":0,"session_state":"6f6b9478-fed4-4048-ba16-6d3f11c95850","scope":"openid email profile"
```

Необходимо скопировать значение отображенное на месте TOKEN_TO_COPY, чтобы сохнарить его в переменную окружения

### Подключение к Gateway

```bash
kubectl port-forward svc/kusk-gateway-envoy-fleet -n kusk-system 8080:80
```

### Вызов API для создания сенсора

```bash
export TOKEN_TO_COPY=Ранее скопированное значение

curl --location 'http://localhost:8080/api/temperature' \
--header 'Content-Type: application/json' \
--header "Authorization: Bearer $TOKEN_TO_COPY" \
--data '{
  "id": 1,
  "currentTemperature": 20.3,
  "lastUpdated": "2024-08-20T22:55:04.425"
}'
```

### Ответ

```json
{
  "id": 1,
  "currentTemperature": 20.3,
  "lastUpdated": "2024-08-20T22:55:04.425"
}
```

### Обновление температуры

Вызовет отправку сообщениея в Kafka, которое прокитает микросервис телеметрии и сохранит в своей базе

```bash
curl --location --request POST 'http://localhost:8080/api/temperature/2/set-temperature?temperature=22.0' \
--header "Authorization: Bearer $TOKEN_TO_COPY"
```

### Проверка трэйса в Jaeger

Запускаем дашборд

```bash
istioctl dashboard jaeger
```

Выбираем сервис istio-ingressgateway.istio-system и нажимаем Find Traces. Появится список трэйсов.
Для последнего сервиса будет отображено дерево:

- istio-ingressgateway.istio-system - Подключение к Gateway
  - smart-home-monolith.default - Подключение к Pod
    - smart-home-monolith.spring POST /api/temperature/{id}/set-temperature - Вызов API в микросервисе
      - smart-home-monolith.spring telemetry publish - Публикация в kafka
        - smart-home-monolith.spring telemetry process - Получение данных из kafka в этом же микросервисе
        - smart-home-telemetry.spring telemetry process - Получение данных из kafka в микросервисе телеметрии
          - smart-home-telemetry.spring HikariDataSource.getConnection - Подклюсение к базе данныйх
          - smart-home-telemetry.spring INSERT smart_home_telemetry.temperature_sensors - Запись в базу данных

### Подключение к Grafana

```bash
istioctl dashboard grafana
```

### Подключение к Prometheus

```bash
istioctl dashboard prometheus
```

### Подключение к Kiali

```bash
istioctl dashboard kiali
```

## Delete minikube

```bash
minikube delete
```
