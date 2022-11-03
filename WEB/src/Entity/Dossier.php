<?php

namespace App\Entity;

use App\Repository\DossierRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\DateType;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=DossierRepository::class)
 */
class Dossier
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;


    /**
     * @ORM\ManyToOne(targetEntity=Medecin::class, inversedBy="dossier")
     */
    private $medecin;

    /**
     * @ORM\ManyToOne(targetEntity=Patient::class, inversedBy="dossier")
     * @Assert\NotBlank(message="Ce champ est obligatoire")
     */
    private $patient;

    /**
     * @ORM\Column(type="date")
     */
    private $datecreation;

    /**
     * @ORM\OneToMany(targetEntity=FicheMedicale::class, mappedBy="dossier", orphanRemoval=true)
     */
    private $fiche;

    public function __construct()
    {

        $this->fiche = new ArrayCollection();
        $this->datecreation = new\DateTime();
    }
    public function __toString():?string
    {
        return $this->id;
    }
    public function getId(): ?int
    {
        return $this->id;
    }
    /**
     * @return Collection|FicheMedicale[]
     */
    public function getFiche(): Collection
    {
        return $this->fiche;
    }

    public function addFiche(FicheMedicale $fiche): self
    {
        if (!$this->fiche->contains($fiche)) {
            $this->fiche[] = $fiche;
            $fiche->setDossier($this);
        }

        return $this;
    }

    public function removeFiche(FicheMedicale $fiche): self
    {
        if ($this->fiche->removeElement($fiche)) {
            // set the owning side to null (unless already changed)
            if ($fiche->getDossier() === $this) {
                $fiche->setDossier(null);
            }
        }

        return $this;
    }



    public function getMedecin(): ?Medecin
    {
        return $this->medecin;
    }

    public function setMedecin(?Medecin $medecin): self
    {
        $this->medecin = $medecin;

        return $this;
    }

    public function getPatient(): ? Patient
    {
        return $this->patient;
    }

    public function setPatient(?Patient $patient): self
    {
        $this->patient = $patient;

        return $this;
    }

    public function getDatecreation(): ?\DateTimeInterface
    {
        return $this->datecreation;
    }

    public function setDatecreation(\DateTimeInterface $datecreation): self
    {
        $this->datecreation = $datecreation;

        return $this;
    }

}
