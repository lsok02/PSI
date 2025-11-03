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

---

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

## Bezpieczeństwo i incydenty

#### RB001: Obowiązkowa kategoryzacja i priorytetyzacja incydentu
Każdy incydent musi mieć przypisaną kategorię (np. medyczny, techniczny) oraz priorytet (np. Krytyczny, Wysoki).

#### RB002: Przypisanie incydentu do zespołu
Dyspozytor musi przypisać incydent o statusie "Nowy" do zespołu o kwalifikacjach zgodnych z kategorią zdarzenia.

#### RB003: Wymóg raportu zamknięcia
Incydent może zostać zamknięty dopiero po wypełnieniu przez zespół interwencyjny raportu z podjętych działań.

#### RB004: Eskalacja incydentu
Incydent o priorytecie "Wysokim" lub "Krytycznym" musi zostać eskalowany do Managera ds. Bezpieczeństwa, jeśli jego status nie zmieni się na "W toku" w ciągu 10 minut od przydzielenia.

#### RB005: Aktywacja procedury komunikacji kryzysowej
Incydent o priorytecie "Krytycznym" (np. pożar, zagrożenie bombowe) wymaga od Dyspozytora natychmiastowego uruchomienia procedury komunikacji kryzysowej.

#### OS001: Automatyczne generowanie incydentów krytycznych
Alarm z zintegrowanego systemu bezpieczeństwa (np. PPOŻ, KD) musi automatycznie wygenerować w systemie incydent o priorytecie "Krytyczny".

#### OS002: Walidacja cyklu życia incydentu
System musi wymuszać zmianę statusu incydentu zgodnie z sekwencją:  
**Nowy → Przydzielony → W toku → Rozwiązany → Zamknięty**.

#### OS003: Wymagalność kluczowych pól
Zapisanie nowego incydentu wymaga podania jego **typu**, **lokalizacji** oraz **źródła zgłoszenia**.

#### OS004: Niezmienność zarchiwizowanych danych
Dane incydentu o statusie "Zamknięty" stają się niezmienialne (**read-only**).

#### OS005: Automatyczne powiadomienia o przydzieleniu zadania
Przypisanie incydentu do zespołu musi automatycznie wyzwolić wysłanie powiadomienia do jego członków, zawierającego **ID incydentu**, **lokalizację** i **priorytet**.

#### OS006: Ograniczenia uprawnień do modyfikacji
Tylko role **Dyspozytor centrum bezpieczeństwa** lub **Administrator systemu** mogą tworzyć i przydzielać incydenty.  
Rola **Członek zespołu interwencyjnego** może jedynie aktualizować status własnych zadań.

#### OS007: Automatyczna integracja z innymi domenami
Incydent dotyczący zasobu lotniskowego (np. bramka, pas startowy) musi automatycznie generować powiadomienie do powiązanych domen (np. **Loty i harmonogramy**).

## Obsługa naziemna i zasoby

#### RB001: Przydział personelu do zadań
Opis: Pracownik może zostać przypisany do zadania tylko, jeśli posiada wymagane kwalifikacje i uprawnienia oraz jest dostępny w danej zmianie.

#### RB002: Przydział sprzętu i pojazdów 
Opis: Sprzęt lub pojazd mogą zostać przypisane do zadania tylko, jeśli mają status „SPRAWNY” i nie są obecnie zarezerwowane ani w przeglądzie.

#### RB003: Zakończenie zadania
Opis: Zadanie może zostać oznaczone jako zakończone dopiero po potwierdzeniu wykonania przez przypisanego pracownika i weryfikacji przez koordynatora.

#### RB004: Raportowanie incydentów
Opis: Każdy incydent lub awaria musi zawierać identyfikator zadania lub zasobu, opis zdarzenia, datę, godzinę i dane osoby zgłaszającej.

#### RB005: Zarządzanie zmianami
Opis: Harmonogram zmian personelu musi być zdefiniowany z co najmniej 24-godzinnym wyprzedzeniem. Modyfikacje wymagają zatwierdzenia przez kierownika zmiany.

#### RB006: Raportowanie stanu zasobów
Opis: Stan techniczny sprzętu i pojazdów musi być aktualizowany minimum raz na 24 godziny. Brak aktualizacji zmienia status zasobu na „DO WERYFIKACJI”.

#### RB007: Powiązanie z lotem
Opis: Zadania operacyjne (np. tankowanie, sprzątanie, załadunek) muszą być powiązane z konkretnym lotem. Wyjątek stanowią testy sprzętu lub szkolenia.

#### RB008: Uprawnienia do modyfikacji zasobów
Opis: Zmiana statusu sprzętu lub pojazdu (np. z „SPRAWNY” na „NIEDOSTĘPNY”) może być wykonana tylko przez technika utrzymania lub kierownika zasobów.

#### RB009: Raporty operacyjne
Opis: System generuje dzienne raporty dotyczące wykorzystania personelu, sprzętu i pojazdów. Raporty są archiwizowane przez minimum 12 miesięcy.

#### RB010: Zgłaszanie awarii krytycznych
Opis: Awaria sprzętu kluczowego (np. cysterny paliwowej, pojazdu transportowego) automatycznie generuje komunikat do domeny „Zarządzanie bezpieczeństwem i incydentami”.

#### OS001: Weryfikacja dostępności personelu
Opis: System nie pozwala przypisać pracownika do więcej niż jednego zadania w tym samym przedziale czasowym.

#### OS002: Weryfikacja dostępności sprzętu
Opis: System blokuje przypisanie zasobu technicznego, który jest w trakcie konserwacji, rezerwacji lub oznaczony jako „NIEDOSTĘPNY”.

#### OS003: Kolejność statusów zadania
Opis: Status zadania może zmieniać się tylko w kolejności: „ZAPLANOWANE” → „W TOKU” → „ZAKOŃCZONE”.

#### OS004: Blokada usuwania aktywnych zasobów
Opis: System nie pozwala usunąć pracownika, pojazdu lub sprzętu przypisanego do aktywnych lub niezakończonych zadań.

#### OS005: Integracja z lotami i harmonogramami
Opis: Zadania powiązane z lotem nie mogą rozpocząć się, dopóki lot nie ma statusu „PRZYGOTOWANY DO OBSŁUGI” w domenie „Loty i harmonogramy”.

#### OS006: Integracja z bezpieczeństwem
Opis: W przypadku aktywnego incydentu lub ograniczenia strefy, system blokuje przydzielanie zadań do lokalizacji objętych ryzykiem.

#### OS007: Walidacja kwalifikacji personelu
Opis: System automatycznie sprawdza zgodność uprawnień pracownika z wymaganiami zadania (np. uprawnienie do obsługi pojazdu paliwowego).