<?php

namespace App\Entity;

use App\Repository\MaladieRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity(repositoryClass=MaladieRepository::class)
 */
class Maladie
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="string", length=25)
     */
    private $nom;
    /**
     * @ORM\OneToMany(targetEntity=FicheMedicale::class, mappedBy="maladie")
     */
    private $fiche;

    public function __construct()
    {
        $this->fiche = new ArrayCollection();
    }

    public function __toString():?string
    {
        return $this->nom;
    }

    public function getId(): ?int
    {
        return $this->id;
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
            $fiche->setMaladie($this);
        }

        return $this;
    }

    public function removeFiche(FicheMedicale $fiche): self
    {
        if ($this->fiche->removeElement($fiche)) {
            // set the owning side to null (unless already changed)
            if ($fiche->getMaladie() === $this) {
                $fiche->setMaladie(null);
            }
        }

        return $this;
    }

}
