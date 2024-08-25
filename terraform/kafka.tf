resource "kubernetes_namespace" "kafka" {
  metadata {
    name = "kafka"
    labels = {
      "istio-injection" = "enabled"
    }
  }
}

resource "helm_release" "kafka" {
    repository = "oci://registry-1.docker.io/bitnamicharts"
    name = "kafka"
    chart = "kafka"
    version = "30.0.5"
    namespace  = "kafka"
    values = ["${file("kafka.yaml")}"]
}