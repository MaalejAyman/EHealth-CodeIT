<?php

namespace App\Entity;

use App\Repository\AnalyseRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\Length;

/**
 * @ORM\Entity(repositoryClass=AnalyseRepository::class)
 */
class Analyse
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="text")
     * @Assert\NotBlank(message="La description de l'analyse est obligatoire est obligatoire")
     *      @Assert\Length(
     *      min = 0,
     *      max = 65535,
     *      minMessage = "Trop court",
     *      maxMessage = "Trop long")
     */
    private $description;

    /**
     * @ORM\Column(type="datetime")
     */
    private $date;

    /**
     * @ORM\ManyToOne(targetEntity=DossierLab::class, inversedBy="Analyse")
     * @ORM\JoinColumn(nullable=false)
     */
    private $dossierLab;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;

        return $this;
    }

    public function getDate(): ?\DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(\DateTimeInterface $date): self
    {
        $this->date = $date;

        return $this;
    }

    public function getDossierLab(): ?DossierLab
    {
        return $this->dossierLab;
    }

    public function setDossierLab(?DossierLab $dossierLab): self
    {
        $this->dossierLab = $dossierLab;

        return $this;
    }
}
