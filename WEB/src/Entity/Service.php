<?php

namespace App\Entity;

use App\Repository\ServiceRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;

/**
 * @ORM\Entity(repositoryClass=ServiceRepository::class)
 */
class Service
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
    private $nomService;

    /**
     * @ORM\ManyToMany(targetEntity=Laboratoire::class, mappedBy="services")
     */
    private $Laboratoires;

    public function __construct()
    {
        $this->Laboratoires = new ArrayCollection();
    }
    public function __toString()
    {
       return $this->nomService;
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomService(): ?string
    {
        return $this->nomService;
    }

    public function setNomService(string $nomService): self
    {
        $this->nomService = $nomService;

        return $this;
    }

    /**
     * @return Collection|Laboratoire[]
     */
    public function getLaboratoires(): Collection
    {
        return $this->Laboratoires;
    }

    public function addLaboratoire(Laboratoire $laboratoire): self
    {
        if (!$this->Laboratoires->contains($laboratoire)) {
            $this->Laboratoires[] = $laboratoire;
        }

        return $this;
    }

    public function removeLaboratoire(Laboratoire $laboratoire): self
    {
        $this->Laboratoires->removeElement($laboratoire);

        return $this;
    }
}
