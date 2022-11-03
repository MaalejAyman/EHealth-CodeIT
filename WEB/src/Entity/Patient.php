<?php

namespace App\Entity;

use App\Repository\PatientRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;


/**
 * @ORM\Entity(repositoryClass=PatientRepository::class)
 * @UniqueEntity(fields={"email"}, message="There is already an account with this email", entityClass="App\Entity\User")
 *
 */
class Patient extends User
{

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
     * @ORM\Column(type="integer")
     * @Assert\NotBlank(message="CIN est obligatoire")
     * @Assert\Length(
     *      min = 8,
     *      max = 8,
     *      minMessage = "votre cin doit contenir {{ limit }} chiffres ",
     *      maxMessage = "votre cin doit contenir {{ limit }} chiffres")
     */

    private $cin;

    /**
     * @ORM\Column(type="string", length=255)
     */
    private $sexe;

    /**
     * @ORM\OneToMany(targetEntity=Rendezvous::class, mappedBy="patient", orphanRemoval=true)
     */
    private $rendezvous;

    /**
     * @ORM\Column(type="integer", nullable=true)
     */
    private $blocked;
    /**
     * @ORM\Column(type="boolean")
     */
    private $blockedRDV;
    /**
     * @ORM\OneToMany(targetEntity=Question::class, mappedBy="patient", orphanRemoval=true)
     */
    private $questions;

    /**
     * @ORM\Column(type="integer", nullable=true)
     */
    private $nbAlert;
    /**
     * @ORM\OneToMany (targetEntity=Dossier::class, mappedBy="patient", orphanRemoval=true)
     */
    private $dossier;
    /**
     * @ORM\OneToMany(targetEntity=DossierLab::class, mappedBy="patient", orphanRemoval=true)
     */
    private $dossierLabs;

    public function __construct()
    {
        $this->nbAlert =0;
        $this->blocked =0;
        $this->blockedRDV =0;
        $this->rendezvous = new ArrayCollection();
        $this->questions = new ArrayCollection();
        $this->dossier = new ArrayCollection();
        $this->dossierLabs = new ArrayCollection();
    }

    public function __toString()
    {
        return $this->getNom()." ".$this->getPrenom()." ".$this->getCin();
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

    public function getCin(): ?int
    {
        return $this->cin;
    }

    public function setCin(int $cin): self
    {
        $this->cin = $cin;

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
            $rendezvou->setPatient($this);
        }

        return $this;
    }

    public function removeRendezvou(Rendezvous $rendezvou): self
    {
        if ($this->rendezvous->removeElement($rendezvou)) {
            // set the owning side to null (unless already changed)
            if ($rendezvou->getPatient() === $this) {
                $rendezvou->setPatient(null);
            }
        }

        return $this;
    }

    public function getBlocked(): ?int
    {
        return $this->blocked;
    }

    public function setBlocked(?int $blocked): self
    {
        $this->blocked = $blocked;

        return $this;
    }

    public function getBlockedRDV(): ?bool
    {
        return $this->blockedRDV;
    }

    public function setBlockedRDV(bool $blockedRDV): self
    {
        $this->blockedRDV = $blockedRDV;

        return $this;
    }
    public function getNbAlert(): ?int
    {
        return $this->nbAlert;
    }

    public function setNbAlert(int $nbAlert): self
    {
        $this->nbAlert = $nbAlert;

        return $this;
    }

    /**
     * @return Collection|Question[]
     */
    public function getQuestions(): Collection
    {
        return $this->questions;
    }

    public function addQuestion(Question $question): self
    {
        if (!$this->questions->contains($question)) {
            $this->questions[] = $question;
            $question->setPatient($this);
        }

        return $this;
    }

    public function removeQuestion(Question $question): self
    {
        if ($this->questions->removeElement($question)) {
            // set the owning side to null (unless already changed)
            if ($question->getPatient() === $this) {
                $question->setPatient(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection|Dossier[]
     */
    public function getDossier(): Collection
    {
        return $this->dossier;
    }

    public function addDossier(Dossier $dossier): self
    {
        if (!$this->dossier->contains($dossier)) {
            $this->dossier[] = $dossier;
            $dossier->setPatient($this);
        }

        return $this;
    }

    public function removeDossier(Dossier $dossier): self
    {
        if ($this->dossier->removeElement($dossier)) {
            // set the owning side to null (unless already changed)
            if ($dossier->getPatient() === $this) {
                $dossier->setPatient(null);
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
            $dossierLab->setPatient($this);
        }

        return $this;
    }

    public function removeDossierLab(DossierLab $dossierLab): self
    {
        if ($this->dossierLabs->removeElement($dossierLab)) {
            // set the owning side to null (unless already changed)
            if ($dossierLab->getPatient() === $this) {
                $dossierLab->setPatient(null);
            }
        }

        return $this;
    }
}
