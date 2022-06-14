use testy_dev;

INSERT IGNORE INTO
  testy_user(
    `id`,
    `firstName`,
    `lastName`,
    `authenticated`,
    `roles`,
    `username`,
    `email`,
    `phone`,
    `password`,
    `course`,
    `activation`,
    `active`
  )
VALUES
  (
    '6489c5a-c02e-4af3-8d24-e55a112813f4',
    'Joey',
    'De Kort',
    null,
    'STUDENT',
    'joey.de.kort',
    'joey.dekort@intecbrussel.be',
    '046123456789',
    '$2a$10$i9I8WtlsSRv9bnZlw.bsWua1SN3qq8dRcZmxwoJpNM/YHnYjHtDw2',
    'BO_JAVA_21_JUNI',
    'ae4321a3-7746-48c1-b6de-efe6cb9d1f48',
    'true'
  ),
  (
    '67b8fe50-cc64-4a69-bf07-505190b5966e',
    'Hilal',
    'Demir',
    null,
    'STUDENT',
    'hilal.demir',
    'hilal.demir@intecbrussel.be',
    '046123456321',
    '$2a$10$i9I8WtlsSRv9bnZlw.bsWua1SN3qq8dRcZmxwoJpNM/YHnYjHtDw2',
    'BO_JAVA_21_JUNI',
    '18571f46-a480-494e-8c80-238089afbc33',
    'true'
  ),
   (
    '697f194a-4bc4-43d0-96ae-ad212686ab87',
    'Irina',
    'Afanassenko',
    null,
    'STUDENT',
    'irina.afanassenko',
    'irina.afanassenko@intecbrussel.be',
    '046123456358',
    '$2a$10$i9I8WtlsSRv9bnZlw.bsWua1SN3qq8dRcZmxwoJpNM/YHnYjHtDw2',
    'BO_JAVA_21_JUNI',
    '1ef60043-5579-4e8e-b269-59dc178998f9',
    'true'
  )