resource "helm_release" "smart-home-telemetry" {
  name       = "smart-home-telemetry"
  namespace  = "default"
  chart      = "../charts/smart-home-telemetry"
}