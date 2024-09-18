resource "helm_release" "smart-home-telemetry" {
  depends_on = [
    helm_release.keycloak
  ]
  name       = "smart-home-telemetry"
  namespace  = "default"
  chart      = "../charts/smart-home-telemetry"
}