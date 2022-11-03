<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20210331230725 extends AbstractMigration
{
    public function getDescription() : string
    {
        return '';
    }

    public function up(Schema $schema) : void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE laboratoire_service (laboratoire_id INT NOT NULL, service_id INT NOT NULL, INDEX IDX_E9036DCD76E2617B (laboratoire_id), INDEX IDX_E9036DCDED5CA9E6 (service_id), PRIMARY KEY(laboratoire_id, service_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE laboratoire_service ADD CONSTRAINT FK_E9036DCD76E2617B FOREIGN KEY (laboratoire_id) REFERENCES laboratoire (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE laboratoire_service ADD CONSTRAINT FK_E9036DCDED5CA9E6 FOREIGN KEY (service_id) REFERENCES service (id) ON DELETE CASCADE');
        $this->addSql('DROP TABLE service_laboratoire');
    }

    public function down(Schema $schema) : void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE service_laboratoire (service_id INT NOT NULL, laboratoire_id INT NOT NULL, INDEX IDX_837256FED5CA9E6 (service_id), INDEX IDX_837256F76E2617B (laboratoire_id), PRIMARY KEY(service_id, laboratoire_id)) DEFAULT CHARACTER SET utf8 COLLATE `utf8_unicode_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('ALTER TABLE service_laboratoire ADD CONSTRAINT FK_837256F76E2617B FOREIGN KEY (laboratoire_id) REFERENCES laboratoire (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE service_laboratoire ADD CONSTRAINT FK_837256FED5CA9E6 FOREIGN KEY (service_id) REFERENCES service (id) ON DELETE CASCADE');
        $this->addSql('DROP TABLE laboratoire_service');
    }
}
