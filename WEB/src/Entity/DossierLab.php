<?php

namespace App\Entity;

use App\Repository\DossierLabRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\Length;


/**
 * @ORM\Entity(repositoryClass=DossierLabRepository::class)
 */
class DossierLab
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\ManyToOne(targetEntity=Laboratoire::class, inversedBy="dossierLabs")
     * @ORM\JoinColumn(nullable=false)
     */
    private $Laboratoire;

    /**
     * @ORM\ManyToOne(targetEntity=Patient::class, inversedBy="dossierLabs")
     * @ORM\JoinColumn(nullable=false)
     * @Assert\NotBlank(message="Ce champ est obligatoire")
     */
    private $patient;

    /**
     * @ORM\OneToMany(targetEntity=Analyse::class, mappedBy="dossierLab", orphanRemoval=true)
     */
    private $Analyse;

    public function __construct()
    {
        $this->Analyse = new ArrayCollection();
    }

    public function _toString()
    {
        return $this->id;
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getLaboratoire(): ?Laboratoire
    {
        return $this->Laboratoire;
    }

    public function setLaboratoire(?Laboratoire $Laboratoire): self
    {
        $this->Laboratoire = $Laboratoire;

        return $this;
    }

    public function getPatient(): ?Patient
    {
        return $this->patient;
    }

    public function setPatient(?Patient $patient): self
    {
        $this->patient = $patient;

        return $this;
    }

    /**
     * @return Collection|Analyse[]
     */
    public function getAnalyse(): Collection
    {
        return $this->Analyse;
    }

    public function addAnalyse(Analyse $analyse): self
    {
        if (!$this->Analyse->contains($analyse)) {
            $this->Analyse[] = $analyse;
            $analyse->setDossierLab($this);
        }

        return $this;
    }

    public function removeAnalyse(Analyse $analyse): self
    {
        if ($this->Analyse->removeElement($analyse)) {
            // set the owning side to null (unless already changed)
            if ($analyse->getDossierLab() === $this) {
                $analyse->setDossierLab(null);
            }
        }

        return $this;
    }
}
