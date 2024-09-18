resource "helm_release" "smart-home-monolith" {
  depends_on = [
    helm_release.keycloak
  ]
  name       = "smart-home-monolith"
  namespace  = "default"
  chart      = "../charts/smart-home-monolith"
}