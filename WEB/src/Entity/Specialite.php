<?php

namespace App\Entity;

use App\Repository\SpecialiteRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;

/**
 * @ORM\Entity(repositoryClass=SpecialiteRepository::class)
 */
class Specialite
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="string", length=255)
     * @Assert\NotBlank(message="la specialité  est obligatoire")
     */
    private $nomSpec;

    /**
     * @ORM\OneToMany(targetEntity=Medecin::class, mappedBy="specialite")
     */
    private $Medecins;

    /**
     * @ORM\OneToMany(targetEntity=Question::class, mappedBy="specialite")
     */
    private $questions;

    public function __construct()
    {
        $this->Medecins = new ArrayCollection();
        $this->questions = new ArrayCollection();
    }
    public function __toString()
    {
        return $this->nomSpec;
    }
    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomSpec(): ?string
    {
        return $this->nomSpec;
    }

    public function setNomSpec(string $nomSpec): self
    {
        $this->nomSpec = $nomSpec;

        return $this;
    }

    /**
     * @return Collection|Medecin[]
     */
    public function getMedecins(): Collection
    {
        return $this->Medecins;
    }

    public function addMedecin(Medecin $medecin): self
    {
        if (!$this->Medecins->contains($medecin)) {
            $this->Medecins[] = $medecin;
            $medecin->setSpecialite($this);
        }

        return $this;
    }

    public function removeMedecin(Medecin $medecin): self
    {
        if ($this->Medecins->removeElement($medecin)) {
            // set the owning side to null (unless already changed)
            if ($medecin->getSpecialite() === $this) {
                $medecin->setSpecialite(null);
            }
        }

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
            $question->setSpecialite($this);
        }

        return $this;
    }

    public function removeQuestion(Question $question): self
    {
        if ($this->questions->removeElement($question)) {
            // set the owning side to null (unless already changed)
            if ($question->getSpecialite() === $this) {
                $question->setSpecialite(null);
            }
        }

        return $this;
    }
}
