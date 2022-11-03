<?php

namespace App\Entity;

use App\Repository\MedecinRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;


/**
 * @ORM\Entity(repositoryClass=MedecinRepository::class)
 * @UniqueEntity(fields={"email"}, message="There is already an account with this email", entityClass="App\Entity\User")
 */
class Medecin extends User
{

    /**
     * @ORM\Column(type="string", length=255)
     * @Assert\NotBlank(message="votre prenom est obligatoire")
     * @Assert\Type(
     * type={"alpha", "digit"},
     * message="Votre nom {{ value }} doit contenir
     * seulement des lettres alphabétiques"
     * )
     */
    private $nom;

    /**
     * @ORM\Column(type="string", length=255)
     * @Assert\NotBlank(message="votre prenom est obligatoire")
     * @Assert\Type(
     * type={"alpha", "digit"},
     * message="Votre nom {{ value }} doit contenir
     * seulement des lettres alphabétiques"
     * )
     */

    private $prenom;

    /**
     * @ORM\Column(type="string", length=255)
     */
    private $sexe;

    /**
     * @ORM\Column(type="time")

     */
    private $horaire_debut;

    /**
     * @ORM\Column(type="time")
     * @Assert\GreaterThan(propertyPath="horaire_debut")
     */
    private $horaire_fin;

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
     * @ORM\ManyToOne(targetEntity=Specialite::class, inversedBy="Medecins")
     * @ORM\JoinColumn(nullable=false)
     */
    private $specialite;

    /**
     * @ORM\OneToMany(targetEntity=Rendezvous::class, mappedBy="medecin", orphanRemoval=true)
     */
    private $rendezvous;

    /**
     * @ORM\Column(type="integer", nullable=true)
     */
    private $note;

    /**
     * @ORM\OneToMany(targetEntity=Event::class, mappedBy="medecin", orphanRemoval=true)
     */
    private $events;
    /**
     * @ORM\OneToMany(targetEntity=Reponse::class, mappedBy="medecin", orphanRemoval=true)
     */
    private $reponses;
    /**
     * @ORM\OneToMany(targetEntity=Dossier::class, mappedBy="medecin", orphanRemoval=true)
     */
    private $dossier;

    /**
     * @return mixed
     */
    public function getDossier()
    {
        return $this->dossier;
    }

    /**
     * @param mixed $dossier
     */
    public function setDossier($dossier): void
    {
        $this->dossier = $dossier;
    }

    public function __construct()
    {
        $this->note=0;
        $this->rendezvous = new ArrayCollection();
        $this->events = new ArrayCollection();
        $this->reponses = new ArrayCollection();
        $this->dossier = new ArrayCollection();
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

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): self
    {
        $this->prenom = $prenom;

        return $this;
    }

    public function getSexe(): ?string
    {
        return $this->sexe;
    }

    public function setSexe(string $sexe): self
    {
        $this->sexe = $sexe;

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

    public function getNumService(): ?int
    {
        return $this->num_service;
    }

    public function setNumService(int $num_service): self
    {
        $this->num_service = $num_service;

        return $this;
    }

    public function getSpecialite(): ?Specialite
    {
        return $this->specialite;
    }

    public function setSpecialite(?Specialite $specialite): self
    {
        $this->specialite = $specialite;

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
            $rendezvou->setMedecin($this);
        }

        return $this;
    }

    public function removeRendezvou(Rendezvous $rendezvou): self
    {
        if ($this->rendezvous->removeElement($rendezvou)) {
            // set the owning side to null (unless already changed)
            if ($rendezvou->getMedecin() === $this) {
                $rendezvou->setMedecin(null);
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
            $event->setMedecin($this);
        }

        return $this;
    }

    public function removeEvent(Event $event): self
    {
        if ($this->events->removeElement($event)) {
            // set the owning side to null (unless already changed)
            if ($event->getMedecin() === $this) {
                $event->setMedecin(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection|Reponse[]
     */
    public function getReponses(): Collection
    {
        return $this->reponses;
    }

    public function addReponse(Reponse $reponse): self
    {
        if (!$this->reponses->contains($reponse)) {
            $this->reponses[] = $reponse;
            $reponse->setMedecin($this);
        }

        return $this;
    }

    public function removeReponse(Reponse $reponse): self
    {
        if ($this->reponses->removeElement($reponse)) {
            // set the owning side to null (unless already changed)
            if ($reponse->getMedecin() === $this) {
                $reponse->setMedecin(null);
            }
        }

        return $this;
    }

    public function addDossier(Dossier $dossier): self
    {
        if (!$this->dossier->contains($dossier)) {
            $this->dossier[] = $dossier;
            $dossier->setMedecin($this);
        }

        return $this;
    }

    public function removeDossier(Dossier $dossier): self
    {
        if ($this->dossier->removeElement($dossier)) {
            // set the owning side to null (unless already changed)
            if ($dossier->getMedecin() === $this) {
                $dossier->setMedecin(null);
            }
        }

        return $this;
    }
}
