## Reguły biznesowe i ograniczenia systemowe

## Pasażerowie i odprawy
**RB001: Weryfikacja dokumentów podróży**
Opis: Każdy pasażer musi posiadać ważny dokument podróży (paszport lub dowód osobisty) zgodny z wymaganiami kraju docelowego.

**RB002: Status rezerwacji a odprawa**
Opis: Odprawa może być przeprowadzona tylko dla rezerwacji o statusie "POTWIERDZONA".

**RB003: Czas odprawy**
Opis: Odprawa musi zostać zakończona minimum 45 minut przed planowanym odlotem.

**RB004: Generowanie karty pokładowej**
Opis: Karta pokładowa jest generowana automatycznie po pomyślnej odprawie i zawiera niezbędne dane do kontroli i boardingu.

**RB005: Zarządzanie bagażem**
Opis: Każdy bagaż rejestrowany musi zostać oznaczony unikalną etykietą zawierającą kod kreskowy/QR.

**OS001: Limit bagażu rejestrowanego**
Opis: Maksymalna waga pojedynczego bagażu rejestrowanego nie może przekraczać 32 kg, a łączna waga zgodna z limitami taryfy.

**OS002: Walidacja miejsc w samolocie**
Opis: System nie może przypisać tego samego miejsca dwóm różnym pasażerom na tym samym locie.

**OS003: Sekwencja kontroli bezpieczeństwa**
Opis: Pasażer może przejść przez kontrolę bezpieczeństwa tylko po pomyślnej odprawie i z ważną kartą pokładową.

**OS004: Warunki boardingu**
Opis: Pasażer może wejść na pokład tylko po pomyślnym przejściu wszystkich etapów kontroli (odprawa, kontrola bezpieczeństwa).

**OS005: Zamknięcie lotu**
Opis: Boarding musi zostać zakończony minimum 20 minut przed planowanym odlotem samolotu.

**OS006: Integracja statusu lotu**
Opis: Odprawa i boarding są blokowane dla lotów o statusie "ODWOŁANY", "W_POWIETRZU" lub "WYLĄDOWAŁ".

**OS008: Limit czasowy modyfikacji**
Opis: Zmiany w rezerwacji mogą być dokonywane tylko do 4 godzin przed odlotem.
