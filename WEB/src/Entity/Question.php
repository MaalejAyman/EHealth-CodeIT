<?php

namespace App\Entity;

use App\Repository\QuestionRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=QuestionRepository::class)
 */
class Question
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="text")
     *  @Assert\NotBlank(message="Ce champ est obligatoire")
     */
    private $text;

    /**
     * @ORM\ManyToOne(targetEntity=Patient::class, inversedBy="questions")
     * @ORM\JoinColumn(nullable=false, referencedColumnName="id")
     */
    private $patient;

    /**
     * @ORM\ManyToOne(targetEntity=Specialite::class, inversedBy="questions")
     * @ORM\JoinColumn(nullable=false)
     */
    private $specialite;



    /**
     * @ORM\OneToMany(targetEntity=Reponse::class, mappedBy="question", orphanRemoval=true)
     */
    private $reponses;


    public function __construct()
    {
        $this->reponses = new ArrayCollection();
    }
    public function __toString() {
        return ($this->text);
    }
    public function getId(): ?int
    {
        return $this->id;
    }
    public function setId(int $text)
    {
        $this->Id = $text;

    }
    public function getText(): ?string
    {
        return $this->text;
    }

    public function setText(string $text): self
    {
        $this->text = $text;

        return $this;
    }

    public function getPatient(): ?patient
    {
        return $this->patient;
    }

    public function setPatient(?patient $patient): self
    {
        $this->patient = $patient;

        return $this;
    }

    public function getSpecialite(): ?specialite
    {
        return $this->specialite;
    }

    public function setSpecialite(?specialite $specialite): self
    {
        $this->specialite = $specialite;

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
            $reponse->setQuestion($this);
        }

        return $this;
    }

    public function removeReponse(Reponse $reponse): self
    {
        if ($this->reponses->removeElement($reponse)) {
            // set the owning side to null (unless already changed)
            if ($reponse->getQuestion() === $this) {
                $reponse->setQuestion(null);
            }
        }

        return $this;
    }

}
