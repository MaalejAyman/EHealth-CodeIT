<?php

namespace App\Entity;

use App\Repository\LaboratoireRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;


/**
 * @ORM\Entity(repositoryClass=LaboratoireRepository::class)
 * @UniqueEntity(fields={"email"}, message="There is already an account with this email", entityClass="App\Entity\User")
 */
class Laboratoire extends User
{

    /**
     * @ORM\Column(type="integer")
     * @Assert\NotBlank(message="numero de service est obligatoire")
     * @Assert\Length(
     *      min = 5,
     *      max = 5,
     *      minMessage = "votre cin doit contenir {{ limit }} chiffres ",
     *      maxMessage = "votre cin doit contenir {{ limit }} chiffres")
     */
    private $num_service;

    /**
     * @ORM\Column(type="string", length=255)
     * @Assert\NotBlank(message="votre nom est obligatoire")
     * @Assert\Type(
     * type={"alpha", "digit"},
     * message="Votre nom {{ value }} doit contenir
     * seulement des lettres alphabétiques")
     */

    private $nom;

    /**
     * @ORM\Column(type="time")
     */
    private $horaire_debut;

    /**
     * @ORM\Column(type="time")
     * @Assert\GreaterThan(propertyPath="horaire_debut",message="horaire de fermeture doit etre superieure au horaire d'ouverture")
     */
    private $horaire_fin;

    /**
     * @ORM\ManyToMany(targetEntity=Service::class, inversedBy="Laboratoires")
     */
    private $services;

    /**
     * @ORM\OneToMany(targetEntity=Rendezvous::class, mappedBy="laboratoire", orphanRemoval=true)
     */
    private $rendezvous;

    /**
     * @ORM\Column(type="integer", nullable=true)
     */
    private $note;

    /**
     * @ORM\OneToMany(targetEntity=Event::class, mappedBy="laboratoire", orphanRemoval=true)
     */
    private $events;
    /**
     * @ORM\OneToMany(targetEntity=DossierLab::class, mappedBy="Laboratoire", orphanRemoval=true)
     */
    private $dossierLabs;



    public function __construct()
    {
        $this->note=0;
        $this->services = new ArrayCollection();
        $this->rendezvous = new ArrayCollection();
        $this->events = new ArrayCollection();
        $this->dossierLabs = new ArrayCollection();
    }
    public function getNumService(): ?int
    {
        return $this->num_service;
    }

    public function setNumService(int $num_service): self
    {
        $this->num_service = $num_service;

        return $this;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;

        return $this;
    }

    public function getHoraireDebut(): ?\DateTimeInterface
    {
        return $this->horaire_debut;
    }

    public function setHoraireDebut(\DateTimeInterface $horaire_debut): self
    {
        $this->horaire_debut = $horaire_debut;

        return $this;
    }

    public function getHoraireFin(): ?\DateTimeInterface
    {
        return $this->horaire_fin;
    }

    public function setHoraireFin(\DateTimeInterface $horaire_fin): self
    {
        $this->horaire_fin = $horaire_fin;

        return $this;
    }

    /**
     * @return Collection|Service[]
     */
    public function getServices(): Collection
    {
        return $this->services;
    }

    public function addService(Service $service): self
    {
        if (!$this->services->contains($service)) {
            $this->services[] = $service;
            $service->addLaboratoire($this);
        }

        return $this;
    }

    public function removeService(Service $service): self
    {
        if ($this->services->removeElement($service)) {
            $service->removeLaboratoire($this);
        }

        return $this;
    }

    /**
     * @return Collection|Rendezvous[]
     */
    public function getRendezvous(): Collection
    {
        return $this->rendezvous;
    }

    public function addRendezvou(Rendezvous $rendezvou): self
    {
        if (!$this->rendezvous->contains($rendezvou)) {
            $this->rendezvous[] = $rendezvou;
            $rendezvou->setLaboratoire($this);
        }

        return $this;
    }

    public function removeRendezvou(Rendezvous $rendezvou): self
    {
        if ($this->rendezvous->removeElement($rendezvou)) {
            // set the owning side to null (unless already changed)
            if ($rendezvou->getLaboratoire() === $this) {
                $rendezvou->setLaboratoire(null);
            }
        }

        return $this;
    }

    public function getNote(): ?int
    {
        return $this->note;
    }

    public function setNote(?int $note): self
    {
        $this->note = $note;

        return $this;
    }

    /**
     * @return Collection|Event[]
     */
    public function getEvents(): Collection
    {
        return $this->events;
    }

    public function addEvent(Event $event): self
    {
        if (!$this->events->contains($event)) {
            $this->events[] = $event;
            $event->setLaboratoire($this);
        }

        return $this;
    }

    public function removeEvent(Event $event): self
    {
        if ($this->events->removeElement($event)) {
            // set the owning side to null (unless already changed)
            if ($event->getLaboratoire() === $this) {
                $event->setLaboratoire(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection|DossierLab[]
     */
    public function getDossierLabs(): Collection
    {
        return $this->dossierLabs;
    }

    public function addDossierLab(DossierLab $dossierLab): self
    {
        if (!$this->dossierLabs->contains($dossierLab)) {
            $this->dossierLabs[] = $dossierLab;
            $dossierLab->setLaboratoire($this);
        }

        return $this;
    }

    public function removeDossierLab(DossierLab $dossierLab): self
    {
        if ($this->dossierLabs->removeElement($dossierLab)) {
            // set the owning side to null (unless already changed)
            if ($dossierLab->getLaboratoire() === $this) {
                $dossierLab->setLaboratoire(null);
            }
        }

        return $this;
    }
}
