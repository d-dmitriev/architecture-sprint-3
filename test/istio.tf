resource "kubernetes_namespace" "istio-system" {
  metadata {
    name = "istio-system"
  }
}

resource "helm_release" "istio_base" {
    repository = "https://istio-release.storage.googleapis.com/charts"
    name = "istio-base"
    chart = "base"
    version = "1.23.0"
    namespace  = "istio-system"
}

resource "helm_release" "istiod" {
    depends_on = [helm_release.istio_base]
    repository = "https://istio-release.storage.googleapis.com/charts"
    name = "istiod"
    chart = "istiod"
    version = "1.23.0"
    namespace  = "istio-system"
}

resource "helm_release" "istio-ingressgateway" {
    depends_on = [helm_release.istiod]
    repository = "https://istio-release.storage.googleapis.com/charts"
    name = "istio-ingressgateway"
    chart = "gateway"
    version = "1.23.0"
    namespace  = "istio-system"
    wait = false
}