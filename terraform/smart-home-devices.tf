resource "helm_release" "smart-home-devices" {
  depends_on = [
    helm_release.keycloak
  ]
  name       = "smart-home-devices"
  namespace  = "default"
  chart      = "../charts/smart-home-devices"
}