provider "google" {
  project = "nth-avatar-386300"
  # region      = "us-central1"
  # zone    = "us-central1-c"
  credentials = file(var.CREDENTIALS_FILE)
}

resource "google_compute_instance" "vm_instance" {
  name         = "tf-instance"
  machine_type = "e2-micro"
  zone         = "us-west4-b"

  boot_disk {
    initialize_params {
      image = "ubuntu-2004-lts"
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }
}

resource "google_compute_network" "vpc_network" {
  name                    = "terraform-network"
  auto_create_subnetworks = "true"
}
