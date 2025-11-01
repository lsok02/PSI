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

## Loty i harmonogramy

### RB001: Walidacja numeru lotu
**Opis:** Numer lotu musi być unikalny w systemie i składać się z kodu linii lotniczej (2 znaki) oraz numeru (1-4 cyfry).

### RB002: Przydział zasobów infrastrukturalnych
**Opis:** Lot może zostać przypisany do pasa startowego, bramki i stanowiska postojowego tylko jeśli są one dostępne w planowanym czasie operacji.

### RB003: Zarządzanie slotami czasowymi
**Opis:** Lot musi posiadać potwierdzony slot czasowy od organów ATC przed wpisaniem go do harmonogramu.

### RB004: Aktualizacja statusu lotu
**Opis:** Status lotu jest automatycznie aktualizowany na "WYSTARTOWAŁ" po otrzymaniu potwierdzenia z systemu ATC.

### RB005: Zarządzanie opóźnieniami
**Opis:** W przypadku opóźnienia przekraczającego 15 minut, system automatycznie powiadamia pasażerów i zespoły obsługi.

### RB006: Tworzenie harmonogramu
**Opis:** Nowy lot może zostać dodany do harmonogramu tylko przez uprawnionego planistę lotów.

### RB007: Weryfikacja konfliktów
**Opis:** System automatycznie weryfikuje konflikty w harmonogramie i alertuje planistę o kolizjach czasowych.

### OS001: Konflikty w harmonogramie
**Opis:** System nie pozwala na przydzielenie tego samego pasa startowego lub bramki dwóm różnym lotom w nakładających się przedziałach czasowych.

### OS002: Walidacja łańcucha przesiadkowego
**Opis:** Minimalny czas pomiędzy kolejnymi lotami tego samego samolotu musi wynosić co najmniej 45 minut (standardowy turnaround).

### OS003: Przypisanie załogi
**Opis:** Załoga musi być przypisana do lotu na minimum 2 godziny przed planowanym odlotem.

### OS004: Zmiana zasobów infrastrukturalnych
**Opis:** Zmiana przydzielonej bramki lub pasa startowego jest możliwa tylko do 30 minut przed planowaną operacją.

### OS005: Integracja z FIDS
**Opis:** System automatycznie publikuje informacje o locie na tablicach FIDS gdy status lotu zmieni się na "OPÓŹNIONY", "ODWOŁANY" lub "WYSTARTOWAŁ".

### OS006: Zarządzanie powiadomieniami
**Opis:** Powiadomienia o zmianach są wysyłane automatycznie do pasażerów gdy opóźnienie przekracza 30 minut lub następuje zmiana bramki.

### OS007: Walidacja danych lotu
**Opis:** Lot musi mieć zdefiniowane: numer lotu, trasę, samolot, slot czasowy i linię lotniczą przed dodaniem do harmonogramu.

### OS008: Monitorowanie stanu floty
**Opis:** System śledzi pozycję samolotów i automatycznie aktualizuje czasy przylotu/odlotu w oparciu o dane z systemu śledzenia floty.

### OS009: Zarządzanie alertami
**Opis:** System wyświetla alerty o krytycznych sytuacjach gdy czas łańcucha przesiadkowego przekracza 90 minut lub brakuje przydzielonych zasobów.

### OS010: Blokada modyfikacji
**Opis:** Modyfikacje harmonogramu są blokowane dla lotów, które mają status "WYSTARTOWAŁ" lub "WYLĄDOWAŁ".

### OS011: Ręczna korekta statusu
**Opis:** Koordynator lotów może ręcznie zmienić status lotu na "OPÓŹNIONY" lub "ODWOŁANY" tylko z obowiązkowym podaniem przyczyny.

### OS012: Sekwencja operacji lotu
**Opis:** Status lotu może zmieniać się tylko zgodnie z sekwencją: ZAPLANOWANY → OPÓŹNIONY/WYSTARTOWAŁ → WYLĄDOWAŁ.
