resource "kubernetes_manifest" "gateway_api_gateway" {
  manifest = {
    apiVersion = "networking.istio.io/v1beta1"
    kind       = "Gateway"
    metadata = {
      namespace = "default"
      name      = "istio-gateway-default"
    }
    spec = {
      selector = {
        istio = "ingressgateway"
      }
      servers = [
        {
          hosts = [
            "*",
          ]
          port = {
            name     = "http"
            number   = 8080
            protocol = "HTTP"
          }
        },
      ]
    }
  }
}

resource "kubernetes_manifest" "smart-home-monolith-lb" {
  manifest = {
    apiVersion = "networking.istio.io/v1"
    kind       = "DestinationRule"
    metadata = {
      name      = "smart-home-monolith"
      namespace = "default"
    }
    spec = {
      host = "smart-home-monolith"
      trafficPolicy = {
        connectionPool = {
          tcp = {
            maxConnections: 2
          }
          http = {
            maxRequestsPerConnection = 2
          }
        }
        loadBalancer = {
          simple = "LEAST_REQUEST"
        }
      }
    }
  }
}

resource "kubernetes_manifest" "smart-home-telemetry-lb" {
  manifest = {
    apiVersion = "networking.istio.io/v1"
    kind       = "DestinationRule"
    metadata = {
      name      = "smart-home-telemetry"
      namespace = "default"
    }
    spec = {
      host = "smart-home-telemetry"
      trafficPolicy = {
        connectionPool = {
          tcp = {
            maxConnections: 2
          }
          http = {
            maxRequestsPerConnection = 2
          }
        }
        loadBalancer = {
          simple = "LEAST_REQUEST"
        }
      }
    }
  }
}

resource "kubernetes_manifest" "smart-home-devices-lb" {
  manifest = {
    apiVersion = "networking.istio.io/v1"
    kind       = "DestinationRule"
    metadata = {
      name      = "smart-home-devices"
      namespace = "default"
    }
    spec = {
      host = "smart-home-devices"
      trafficPolicy = {
        connectionPool = {
          tcp = {
            maxConnections: 2
          }
          http = {
            maxRequestsPerConnection = 2
          }
        }
        loadBalancer = {
          simple = "LEAST_REQUEST"
        }
      }
    }
  }
}

resource "kubernetes_manifest" "smart-home" {
  manifest = {
    apiVersion = "networking.istio.io/v1alpha3"
    kind       = "VirtualService"
    metadata = {
      name      = "smart-home"
      namespace = "default"
    }
    spec = {
      gateways = [
        "default/istio-gateway-default",
      ]
      hosts = [
        "*"
      ]
      http = [
        {
          match = [
            {
              uri = {
                prefix: "/api/heating"
              }
            },
            {
              uri = {
                prefix: "/api/temperature"
              }
            }
          ]
          route = [
            {
              destination = {
                host = "smart-home-monolith"
                port = {
                  number = 8080
                }
              }
            }
          ]
        },
        {
          match = [
            {
              uri = {
                regex: "/api/devices/*/telemetry"
              }
            }
          ]
          route = [
            {
              destination = {
                host = "smart-home-telemetry"
                port = {
                  number = 8080
                }
              }
            }
          ]
        },
        {
          match = [
            {
              method = {
                exact: "POST"
              }
              uri = {
                prefix: "/api/devices"
              }
            },
            {
               method = {
                exact: "GET"
              }
              uri = {
                regex: "/api/devices/*"
              }
            },
            {
              method = {
                exact: "PUT"
              }
              uri = {
                regex: "/api/devices/*/status"
              }
            },
            {
              method = {
                exact: "POST"
              }
              uri = {
                regex: "/api/devices/*/commands"
              }
            }
          ]
          route = [
            {
              destination = {
                host = "smart-home-devices"
                port = {
                  number = 8080
                }
              }
            }
          ]
        }
      ]
    }
  }
}
resource "kubernetes_labels" "istio-injection" {
  api_version = "v1"
  kind        = "Namespace"
  metadata {
    name = "default"
  }
  labels = {
    "istio-injection" = "enabled"
  }
}

resource "kubernetes_manifest" "request_authentication" {
  depends_on = [
    helm_release.keycloak
  ]
  manifest = {
    apiVersion = "security.istio.io/v1beta1"
    kind       = "RequestAuthentication"
    metadata = {
      name      = "default-jwt-api"
      namespace = "istio-system"
    }
    spec = {
      selector = {
        matchLabels = {
          app = "istio-ingressgateway"
        }
      }
      jwtRules = [
        {
          issuer  = "http://localhost:8081/realms/master"
          jwksUri = "http://keycloak.keycloak.svc.cluster.local/realms/master/protocol/openid-connect/certs"
        }
      ]
    }
  }
}

resource "kubernetes_manifest" "require_jwt" {
  depends_on = [
    helm_release.keycloak
  ]
  manifest = {
    apiVersion = "security.istio.io/v1beta1"
    kind       = "AuthorizationPolicy"
    metadata = {
      name      = "require-jwt"
      namespace = "istio-system"
    }
    spec = {
      selector = {
        matchLabels = {
          app = "istio-ingressgateway"
        }
      }
      action = "ALLOW"
      rules = [
        {
          when = [
            {
              key = "request.auth.claims[azp]"
              values: ["admin-cli"]
            }
          ]
        }
      ]
    }
  }
}