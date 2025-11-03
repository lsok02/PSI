# Wyniki etapu I: Modelowanie biznesowe, specyfikacja i analiza wymagań

# Implementacja systemu ERP do zarządzania operacjami i zasobami komercyjnego portu lotniczego

---

**Skład zespołu:** 
+ Michał Klatkowski
+ Dominik Patrzek
+ Aleksandra Pluta
+ Łukasz Sokołowski

**Prowadzący:**
+ dr inż. Marcin Kawalerowicz

---

## Model biznesowy

### Cel i zakres projektu

Celem projektu jest implementacja zintegrowanego systemu ERP, który skonsoliduje zarządzanie kluczowymi operacjami i zasobami wrocławskiego portu lotniczego. System ma zapewnić efektywność operacyjną, bezpieczeństwo oraz płynność procesów poprzez centralizację danych, automatyzację procesów i koordynację działań pomiędzy wszystkimi kluczowymi domenami funkcjonalnymi lotniska.


Projekt obejmuje implementację i integrację czterech kluczowych, wzajemnie powiązanych domen:

**1. Loty i Harmonogramy**
+ Planowanie i Zarządzanie Harmonogramem: Tworzenie, modyfikacja i utrzymanie harmonogramów lotów, alokacja slotów i zasobów lotniskowych (bramki, pasy).
+ Monitorowanie Statusu w Czasie Rzeczywistym: Śledzenie statusu (planowany, opóźniony, odwołany, wylądował, wystartował).
+ Komunikacja: Automatyczna aktualizacja systemów FIDS i powiadamianie załóg o zmianach.

**2. Pasażerowie i Odprawy**
+ Proces Odprawy: Wsparcie dla odprawy online i stacjonarnej (check-in), weryfikacja dokumentów i rezerwacji.
+ Zarządzanie Bagażem: Rejestracja, etykietowanie i przekazywanie danych o bagażu do obsługi naziemnej.
+ Kontrola i Boarding: Obsługa bramek kontroli bezpieczeństwa i bramek wejściowych (boarding), weryfikacja kart pokładowych.
+ Rezerwacja i Sprzedaż: Obsługa interfejsu do rezerwacji i zakupu biletów przez pasażerów.
+ 
**3. Obsługa Naziemna i Zasoby**
+ Zarządzanie Zasobami: Ewidencja, monitorowanie stanu technicznego i dostępności sprzętu, pojazdów oraz personelu.
+ Przydzielanie Zadań: Algorytmiczne i manualne przydzielanie personelu i sprzętu do operacji naziemnych (tankowanie, catering, załadunek bagażu).
+ Monitoring Realizacji: Śledzenie postępu zadań w czasie rzeczywistym i raportowanie gotowości do odlotu.

**4. Zarządzanie Bezpieczeństwem i Incydentami**
+ Rejestracja Incydentów: Centralna baza do rejestracji, kategoryzacji i priorytetyzacji wszystkich zdarzeń (automatycznych z systemów fizycznych i manualnych).
+ Koordynacja Reakcji: Panel dyspozytora do zarządzania zespołami interwencyjnymi i monitorowania działań.
+ System Powiadomień: Dystrybucja alertów i komunikatów kryzysowych do odpowiedniego personelu lotniska.

### Słownik pojęć

| Termin                                  | Synonimy   | Definicja terminu                                                                                                                                     |
|-----------------------------------------|------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| Pasażer                                 |            | Osoba podróżująca samolotem, posiadająca bilet i podlegająca procesowi odprawy.                                                                       |
| Bilet                                   |            | Dokument potwierdzający rezerwację pasażera na konkretny lot.                                                                                         |
| Bagaż                                   |            | Rzeczy należące do pasażera, przewożone w luku bagażowym lub jako bagaż podręczny.                                                                    |
| Karta pokładowa                         |            | Dokument uprawniający do wejścia na pokład samolotu. Zawiera dane pasażera, numer lotu i miejsca.                                                     |
| Odprawa                                 |            | Proces rejestracji pasażera i bagażu przed lotem, zakończony wydaniem karty pokładowej.                                                               |
| Kontrola bezpieczeństwa                 |            | Proces weryfikacji tożsamości pasażera oraz skanowania i inspekcji bagażu podręcznego, autoryzujący przejście pasażera do strefy oczekiwania.         |
| Boarding                                |            | Ostateczny proces wejścia pasażerów na pokład samolotu przez przypisaną bramkę, weryfikowany poprzez skanowanie karty pokładowej.                     |
| Strefa oczekiwania                      |            | Obszar w strefie zastrzeżonej (za kontrolą bezpieczeństwa), gdzie pasażerowie oczekują na wywołanie do boardingu przy danej bramce.                   |
| Incydent                                | 	Zdarzenie |	Każde zarejestrowane w systemie zdarzenie (np. medyczne, techniczne, bezpieczeństwa), które zakłóca normalne operacje lotniska i wymaga podjęcia określonych działań.|
| Członek Zespołu Interwencyjnego         | 	          | Pracownik (np. ochroniarz, ratownik medyczny) będący częścią zespołu zadysponowanego do obsługi incydentu, wykonujący zadania w terenie.              |
| Dyspozytor Centrum Bezpieczeństwa       | 	Operator  | Pracownik odpowiedzialny za monitorowanie systemów bezpieczeństwa, rejestrację incydentów, koordynację i przydzielanie zadań zespołom interwencyjnym. |
| Manager ds. Bezpieczeństwa              |            | Osoba odpowiedzialna za nadzór nad całością procesów bezpieczeństwa, analizę danych, raportowanie i doskonalenie procedur.                |
| Standardowa Procedura Operacyjna (SOP)  | 	          |Zdefiniowany zestaw kroków i działań, które muszą zostać podjęte w odpowiedzi na określony typ incydentu.|
| Lot | Rejs, Przelot | Zaplanowana podróż powietrzna pomiędzy portem startowym a docelowym, posiadająca unikalny numer, trasę i harmonogram. |
| Harmonogram lotów | Rozkład lotów | Kompleksowy plan wszystkich lotów uwzględniający czasy, trasy, przydzielone zasoby i sloty czasowe. |
| Slot czasowy | Okno czasowe | Przydzielony przez organy kontroli ruchu lotniczego przedział czasu, w którym samolot może wystartować lub wylądować. |
| Status lotu | Stan lotu | Bieżący stan operacyjny lotu: planowany, opóźniony, odwołany, wystartował, wylądował. |
| Koordynator lotów | Dyspozytor lotów | Pracownik odpowiedzialny za monitorowanie i zarządzanie operacjami lotniczymi w czasie rzeczywistym, wprowadzanie zmian w harmonogramie. |
| Turn-around | Łańcuch przesiadkowy | Proces przygotowania samolotu do kolejnego lotu, obejmujący obsługę naziemną, tankowanie, czyszczenie i załadunek. |
| Planista lotów | | Pracownik odpowiedzialny za tworzenie i utrzymywanie harmonogramów lotów, uwzględniając sloty i dostępność zasobów. |
| Bramka | Gate | Stanowisko, przez które pasażerowie wchodzą na pokład samolotu, posiadające określoną lokalizację i infrastrukturę. |
| Pas startowy | Droga startowa | Infrastruktura lotniskowa przeznaczona do startów i lądowań samolotów. |
| Stanowisko postojowe | Parking samolotu | Wyznaczone miejsce na płycie lotniska do postoju i obsługi naziemnej samolotu. |
| Opóźnienie | | Różnica pomiędzy planowanym a rzeczywistym czasem operacji lotniczej, wymagająca zarządzania zmianami. |
| Załoga | Personel pokładowy | Piloci i personel pokładowy odpowiedzialni za bezpieczne przeprowadzenie lotu. |
| Obsługa naziemna | | Zespoły odpowiedzialne za obsługę samolotu na ziemi: catering, tankowanie, bagaż, czyszczenie. |
| System śledzenia floty | | Technologia umożliwiająca monitorowanie pozycji i statusu samolotów w czasie rzeczywistym. |
| Pracownik naziemny | | Osoba odpowiedzialna za realizację operacji naziemnych, takich jak tankowanie, załadunek bagaży, sprzątanie lub obsługa sprzętu technicznego. |
| Zmiana | dyżur | Określony przedział czasu, w którym pracownicy naziemni pełnią swoje obowiązki operacyjne. |
| Zadanie | operacja | Jednostka pracy wykonywana w ramach obsługi naziemnej, np. tankowanie, sprzątanie, załadunek bagaży; posiada status i przypisane zasoby. |
| Zasób techniczny | | Ogólne pojęcie obejmujące pojazdy i sprzęt specjalistyczny wykorzystywany w obsłudze naziemnej. |
| Pojazd | autobus, ciągnik, cysterna | Typ zasobu technicznego wykorzystywany do transportu ludzi, sprzętu lub materiałów po płycie lotniska. |
| Sprzęt specjalistyczny | narzędzie techniczne, urządzenie obsługowe | Zasób techniczny niebędący pojazdem, np. agregat, schody pasażerskie, podnośnik, wózek bagażowy. |
| Status zasobu | stan zasobu | Aktualny stan dostępności zasobu: *dostępny*, *w użyciu*, *w serwisie*, *niesprawny*. |
| Status zadania  | etap realizacji | Aktualny etap realizacji zadania: *zaplanowane*, *w toku*, *zakończone*, *opóźnione*, *anulowane*. |
| Incydent operacyjny | awaria | Niezaplanowane zdarzenie wpływające na realizację zadań, np. awaria sprzętu lub błąd personelu. |
| Kwalifikacje | uprawnienia, certyfikaty | Zestaw umiejętności i licencji uprawniających pracownika do wykonywania określonych zadań lub obsługi sprzętu. |
| Przegląd techniczny | kontrola techniczna | Zaplanowana czynność konserwacyjna wykonywana w celu utrzymania sprawności i bezpieczeństwa zasobu. |
| Raport operacyjny | | Dokument zawierający dane o wykonanych zadaniach, użytych zasobach i wystąpionych incydentach. |

---

## Specyfikacja i analiza wymagań

### 1. Wymagania funkcjonalne

#### Pasażerowie i odprawy

1. Jako pasażer chcę móc wyszukać lot i zakupić bilet online, aby zagwarantować sobie miejsce na wybranym locie.
2. Jako pasażer chcę mieć możliwość edycji danych rezerwacji, aby dostosować je przed podróżą.
3. Jako pasażer chcę móc anulować swoją rezerwację i otrzymać zwrot zgodnie z zasadami taryfy, aby uniknąć strat finansowych.
4. Jako pasażer chcę widzieć aktualny status mojego lotu, aby wiedzieć czy występują opóźnienia lub zmiany bramki.
5. Jako pasażer chcę móc odprawić się online, wybrać lub zmienić miejsce oraz pobrać kartę pokładową, aby skrócić czas na lotnisku.
6. Jako pasażer chcę móc odprawić się przy stanowisku na lotnisku, aby uzyskać pomoc pracownika obsługi.
7. Jako pasażer chcę móc nadać bagaż rejestrowany przy stanowisku odprawy lub samoobsługowym stanowisku, aby mój bagaż został załadowany do samolotu.
8. Jako pasażer chcę otrzymać cyfrową lub papierową kartę pokładową, aby móc przejść przez wszystkie etapy kontroli.
9. Jako pasażer chcę, aby moja karta pokładowa została zeskanowana i zweryfikowana przy wejściu do kontroli bezpieczeństwa, aby system potwierdził mój status odprawy i uprawnienie do przejścia dalej.
10. Jako pasażer chcę przy bramce zeskanować kartę pokładową, aby móc wejść na pokład samolotu.
11. Jako pasażer chcę otrzymywać powiadomienia o zmianie bramki lub opóźnieniu, aby nie przegapić wejścia na pokład.
12. Jako pracownik odprawy chcę móc zweryfikować dane pasażera, status rezerwacji i dokumenty podróży, aby prawidłowo przeprowadzić proces odprawy.
13. Jako pracownik odprawy chcę móc wprowadzić informacje o bagażu rejestrowanym i wydrukować etykiety bagażowe, aby zapewnić prawidłowe załadowanie bagażu.
14. Jako pracownik odprawy chcę móc zarządzać przypisaniem miejsc.
15. Jako pracownik ochrony chcę móc potwierdzić przejście pasażera przez kontrolę na podstawie skanu karty pokładowej, aby zapewnić bezpieczeństwo operacji.
16. Jako pracownik ochrony chcę móc przeskanować bagaż pasażera.
17. Jako pracownik gate’u chcę móc skanować karty pokładowe podczas boardingu, aby zweryfikować uprawnienie pasażera do wejścia na pokład.
18. Jako pracownik gate’u chcę mieć dostęp do aktualnej listy pasażerów, aby zweryfikować obecność i reagować na braki.
19. Jako pracownik gate’u chcę oznaczać pasażerów, którzy weszli na pokład, aby obsługa lotu mogła potwierdzić kompletność.
20. Jako pracownik gate’u chcę mieć możliwość oznaczenia zakończenia boardingu, aby przekazać gotowość do wykonania operacji lotniczej.

#### Bezpieczeństwo i incydenty

21. Jako dyspozytor centrum bezpieczeństwa chcę móc ręcznie zarejestrować nowy incydent, podając jego lokalizację, typ i opis, aby natychmiast rozpocząć oficjalną procedurę jego obsługi.
22. Jako dyspozytor centrum bezpieczeństwa chcę widzieć na pulpicie alarmy przychodzące automatycznie z zintegrowanych systemów (np. czujnik dymu, alarm z kontroli dostępu), aby móc bezzwłocznie na nie zareagować.
24. Jako dyspozytor centrum bezpieczeństwa chcę mieć możliwość kategoryzacji i priorytetyzacji incydentów (np. medyczny, zagrożenie bezpieczeństwa, techniczny), aby przypisać do nich odpowiednie zasoby i procedury.
25. Jako dyspozytor centrum bezpieczeństwa chcę widzieć wszystkie aktywne incydenty na interaktywnej mapie lotniska, aby mieć pełen obraz sytuacji operacyjnej w czasie rzeczywistym.
26. Jako dyspozytor centrum bezpieczeństwa chcę móc przydzielić incydent do najbliższego dostępnego zespołu interwencyjnego, uwzględniając jego kwalifikacje, aby zoptymalizować czas reakcji.
27. Jako dyspozytor centrum bezpieczeństwa chcę na bieżąco monitorować status każdego incydentu (nowy, przydzielony, w toku, rozwiązany), aby śledzić postępy i w razie potrzeby eskalować problem.
28. Jako dyspozytor centrum bezpieczeństwa chcę móc uruchomić procedurę alarmową i wysłać powiadomienia do zdefiniowanych grup pracowników (np. obsługa naziemna, personel terminala), aby zapewnić szybką i skoordynowaną komunikację kryzysową.
29. Jako dyspozytor centrum bezpieczeństwa chcę mieć dostęp do zarchiwizowanych incydentów wraz z pełną historią podjętych działań, aby móc analizować przeszłe zdarzenia i tworzyć raporty.
30. Jako członek zespołu interwencyjnego chcę otrzymywać na urządzenie mobilne powiadomienia o przydzielonym zadaniu wraz ze wszystkimi szczegółami (lokalizacja, typ incydentu, priorytet), aby móc szybko podjąć działania.
31. Jako członek zespołu interwencyjnego chcę mieć możliwość aktualizacji statusu mojego zadania w systemie (np. "przyjęte", "w drodze", "zakończone"), aby dyspozytor miał wgląd w postęp moich działań w czasie rzeczywistym.
32. Jako członek zespołu interwencyjnego chcę móc zaraportować rozwiązanie incydentu, dodając notatki lub załączając zdjęcia, aby udokumentować wykonane czynności.
33. Jako manager ds. bezpieczeństwa lotniska chcę mieć dostęp do pulpitu analitycznego z kluczowymi wskaźnikami, takimi jak liczba incydentów, średni czas reakcji czy najczęstsze typy zdarzeń, aby oceniać skuteczność procedur bezpieczeństwa.
34. Jako manager ds. bezpieczeństwa lotniska chcę móc generować szczegółowe raporty z incydentów za wybrany okres, aby analizować trendy i identyfikować obszary podwyższonego ryzyka.
35. Jako administrator systemu chcę mieć możliwość konfigurowania integracji z zewnętrznymi systemami bezpieczeństwa (CCTV, kontrola dostępu, system ppoż.), aby zapewnić automatyczny przepływ danych do systemu ERP.
36. Jako administrator systemu chcę móc definiować i zarządzać typami incydentów, priorytetami oraz standardowymi procedurami operacyjnymi (SOP), aby dostosować system do specyficznych potrzeb lotniska.

#### Loty i harmonogramy

37. Jako planista lotów chcę móc tworzyć nowe loty w systemie, definiując ich numer, trasę, planowane czasy i typ samolotu, aby utworzyć podstawowy harmonogram.
38. Jako planista lotów chcę móc przeglądać i zarządzać slotami czasowymi przyznanymi przez organy kontroli ruchu lotniczego, aby zapewnić zgodność z regulacjami.
39. Jako planista lotów chcę, aby system automatycznie przypisywał lotom dostępne pasy startowe i bramki na podstawie harmonogramu, aby zoptymalizować wykorzystanie infrastruktury.
40. Jako koordynator lotów chcę widzieć na pulpicie aktualny status wszystkich lotów w czasie rzeczywistym, aby mieć pełny obraz sytuacji operacyjnej.
41. Jako koordynator lotów chcę, aby system automatycznie aktualizował status lotu na "wystartował" i "wylądował" po otrzymaniu informacji z systemu ATC.
42. Jako koordynator lotów chcę móc ręcznie zmienić status lotu na "opóźniony" lub "odwołany" w przypadku błędów automatycznej aktualizacji.
43. Jako koordynator lotów chcę widzieć szacowany czas opóźnienia dla danego lotu oraz jego przyczynę, aby podejmować świadome decyzje.
44. Jako koordynator lotów chcę móc wprowadzić zmianę bramki dla odlotu lub przylotu, aby system automatycznie powiadomił zespoły obsługi naziemnej i FIDS.
45. Jako koordynator lotów chcę móc wprowadzić zmianę pasa startowego dla danego lotu, aby system powiadomił wieżę kontroli i załogę.
46. Jako koordynator lotów chcę móc zarządzać łańcuchem przesiadkowym, śledząc postęp przygotowania samolotu do kolejnego lotu.
47. Jako system chcę automatycznie publikować aktualny status lotu na tablicach informacyjnych FIDS w terminalu.
48. Jako system chcę automatycznie wysyłać powiadomienia do pasażerów o istotnych zmianach (opóźnienie, zmiana bramki).
49. Jako koordynator lotów chcę móc wysłać wiadomość do załogi konkretnego lotu o zmianach w harmonogramie.
50. Jako system chcę automatycznie przekazywać harmonogram obsługi naziemnej do systemu zarządzania obsługą naziemną.
51. Jako system chcę automatycznie powiadamiać zespoły obsługi naziemnej o zmianie stanowiska postojowego lub czasie przybycia samolotu.
52. Jako koordynator lotów chcę móc filtrować i sortować listę lotów według statusu, linii lotniczej lub bramki.
53. Jako koordynator lotów chcę, aby system wyświetlał alerty o krytycznych sytuacjach na głównym pulpicie.
54. Jako planista lotów chcę, aby system weryfikował konflikty w harmonogramie i alertował o nich.

#### Obsługa naziemna i zasoby
55.	Jako kierownik obsługi naziemnej chcę móc rejestrować nowych pracowników w systemie wraz z ich kwalifikacjami i uprawnieniami, aby móc ich później przypisywać do odpowiednich zadań.
56.	Jako kierownik obsługi naziemnej chcę móc przeglądać listę pracowników wraz z ich dostępnością i przypisaniem do zmian, aby efektywnie planować obsadę.
57.	Jako planista zadań chcę móc tworzyć i planować zadania związane z obsługą samolotu (np. tankowanie, sprzątanie, załadunek bagażu), aby zapewnić ciągłość operacji naziemnych.
58.	Jako planista zadań chcę, aby system automatycznie sugerował dostępny personel i sprzęt na podstawie kwalifikacji i dostępności, aby przyspieszyć proces planowania.
59.	Jako koordynator obsługi naziemnej chcę mieć możliwość przypisania konkretnych zasobów (pojazdy, sprzęt, personel) do danego zadania, aby zapewnić jego realizację.
60.	Jako koordynator obsługi naziemnej chcę móc w czasie rzeczywistym monitorować status realizowanych zadań (w toku, zakończone, opóźnione), aby reagować na problemy.
61.	Jako operator sprzętu chcę móc aktualizować status zadania (rozpoczęte, zakończone, awaria) z poziomu urządzenia mobilnego, aby dane w systemie były aktualne.
62.	Jako kierownik obsługi naziemnej chcę, aby system umożliwiał planowanie zmian i harmonogramów pracy, aby zapewnić ciągłość operacyjną.
63.	Jako technik utrzymania chcę móc rejestrować sprzęt i pojazdy w systemie wraz z ich parametrami i stanem technicznym, aby prowadzić pełną ewidencję zasobów.
64.	Jako technik utrzymania chcę otrzymywać powiadomienia o zbliżających się przeglądach i konserwacjach sprzętu, aby utrzymać jego gotowość operacyjną.
65.	Jako kierownik zasobów chcę móc sprawdzić dostępność sprzętu i pojazdów w danym momencie, aby uniknąć konfliktów w ich przydziale.
66.	Jako koordynator załadunku chcę planować i nadzorować proces załadunku i rozładunku bagażu, aby zapewnić zgodność z planem lotu.
67.	Jako koordynator załadunku chcę, aby system automatycznie uwzględniał dane o liczbie pasażerów i bagaży z domeny odpraw, aby zoptymalizować załadunek.
68.	Jako członek obsługi bagażowej chcę móc raportować problemy z załadunkiem (np. brak wózka, uszkodzony taśmociąg), aby kierownik mógł szybko reagować.
69.	Jako kierownik obsługi naziemnej chcę generować raporty o wykonanych zadaniach, wykorzystaniu personelu i sprzętu, aby analizować efektywność operacyjną.
70.	Jako kierownik obsługi naziemnej chcę, aby system umożliwiał raportowanie awarii sprzętu i incydentów operacyjnych, aby dane były przekazywane do systemu bezpieczeństwa.
71.	Jako system chcę automatycznie przekazywać dane o dostępności personelu i sprzętu do domeny „Loty i harmonogramy”, aby umożliwić planowanie obsługi lotów.
72.	Jako system chcę odbierać informacje o planowanych lotach i ich statusach z domeny „Loty i harmonogramy”, aby przygotować odpowiednie zadania obsługi naziemnej.
73.	Jako system chcę przekazywać zgłoszenia o awariach i incydentach do domeny „Zarządzanie bezpieczeństwem i incydentami”, aby umożliwić odpowiednią reakcję.
74.	Jako system chcę odbierać komunikaty o ograniczeniach dostępu i zagrożeniach z domeny bezpieczeństwa, aby wykluczyć określone strefy z planowania zadań.
75.	Jako kierownik obsługi naziemnej chcę móc przeglądać historię zadań i incydentów związanych z konkretnym personelem lub sprzętem, aby ocenić niezawodność i wydajność.


### 2. Wymagania niefunkcjonalne

#### Dostępność

*   **WNF-1:** System ERP musi zapewniać dostępność na poziomie **99,99%** w skali roku dla wszystkich krytycznych modułów (Loty i harmonogramy, Pasażerowie i odprawy, Bezpieczeństwo i incydenty). Oznacza to maksymalnie 52 minuty niedostępności w ciągu roku.
*   **WNF-2:** Planowane przerwy konserwacyjne nie mogą przekraczać 2 godzin miesięcznie i muszą odbywać się w godzinach najmniejszego ruchu lotniczego (np. między 01:00 a 04:00 w nocy), po uprzednim powiadomieniu administratorów z tygodniowym wyprzedzeniem.

#### Wydajność

*   **WNF-3:** Czas odświeżenia informacji o statusie lotu na publicznych wyświetlaczach oraz w panelach personelu nie może być dłuższy niż **5 sekund** od momentu wprowadzenia zmiany w centralnym module harmonogramów.
*   **WNF-4:** Czas odpowiedzi interfejsu użytkownika dla standardowych operacji (np. wyszukiwanie pasażera, przydzielanie zadania obsłudze naziemnej, otwieranie szczegółów incydentu) nie może przekroczyć **2 sekund** przy jednoczesnej pracy 500 użytkowników w systemie.
*   **WNF-5:** System musi być w stanie przetworzyć co najmniej **5000 transakcji operacyjnych na minutę** (np. skanowanie kart pokładowych przy boardingu, aktualizacja statusu zadania przez obsługę naziemną, logi z systemu kontroli dostępu) bez widocznej degradacji wydajności.

#### Bezpieczeństwo

*   **WNF-6:** Dostęp do poszczególnych modułów i danych musi być kontrolowany przez mechanizm ról i uprawnień. Pracownik obsługi naziemnej nie może mieć dostępu do danych medycznych z incydentów, a dyspozytor bezpieczeństwa do edycji harmonogramu lotów.
*   **WNF-7:** Wszystkie operacje modyfikujące krytyczne dane (zmiana statusu lotu, zakończenie boardingu, zamknięcie incydentu, edycja danych pasażera) muszą być rejestrowane w niezmienialnym dzienniku audytowym (audit log), zawierającym ID użytkownika, znacznik czasu i opis operacji.
*   **WNF-8:** Cała komunikacja sieciowa pomiędzy komponentami systemu oraz z urządzeniami mobilnymi musi być szyfrowana z użyciem protokołu **TLS 1.2 lub nowszego**.

#### Integralność i Odzyskiwanie Danych

*   **WNF-9:** Maksymalny dopuszczalny punkt utraty danych dla krytycznych domen (Loty, Pasażerowie, Incydenty) wynosi **1 minutę**. System musi posiadać mechanizm ciągłej replikacji lub częstych backupów, aby sprostać temu wymaganiu.
*   **WNF-10:** Czas odtworzenia kluczowych funkcjonalności systemu po katastrofalnej awarii nie może przekroczyć **60 minut**.

#### Użyteczność

*   **WNF-11:** Czas potrzebny na wykonanie zdefiniowanych, krytycznych operacji przez przeszkolonego użytkownika nie może przekraczać ustalonych progów. Przykładowo:
    *   Zarejestrowanie nowego incydentu, przypisanie go do zespołu i wysłanie powiadomienia przez dyspozytora: **poniżej 90 sekund**.
    *   Odprawienie pasażera z bagażem rejestrowanym przez pracownika check-in: **poniżej 2 minut**.
    *   Zmiana bramki dla lotu i potwierdzenie aktualizacji przez koordynatora lotów: **poniżej 60 sekund**.
*   **WNF-12:** Krytyczne alerty w panelu dyspozytora bezpieczeństwa muszą być wizualnie odróżnione od pozostałych powiadomień i wymagać aktywnego potwierdzenia przez operatora, aby zminimalizować ryzyko ich przeoczenia.

### 3. Model informacyjny

---

#### Pasażerowie i odprawy

![Diagram - Pasażerowie i odprawy](/Etap%20I/Diagram_klas/Diagram_klas_pasażerowie_i_odprawy.png)

---

#### Loty i harmonogramy

![Diagram - Loty i harmonogramy](/Etap%20I/Diagram_klas/Diagram_klas_loty_i_harmonogramy.png)

---

#### Bezpieczeństwo i incydenty

![Diagram - Bezpieczeństwo i incydenty](/Etap%20I/Diagram_klas/Diagram_klas_bezpieczenstwo_i_incydenty.png)

---

#### Obsługa naziemna i zasoby

![Diagram - Obsługa naziemna i zasoby](/Etap%20I/Diagram_klas/Diagram_klas_obsluga_naziemna_i_zasoby.png)

---

### 4. Reguły biznesowe i ograniczenia systemowe

#### Pasażerowie i odprawy
* **RB-PIO001: Weryfikacja dokumentów podróży**
Opis: Każdy pasażer musi posiadać ważny dokument podróży (paszport lub dowód osobisty) zgodny z wymaganiami kraju docelowego.

* **RB-PIO002: Status rezerwacji a odprawa**
Opis: Odprawa może być przeprowadzona tylko dla rezerwacji o statusie "POTWIERDZONA".

* **RB-PIO003: Czas odprawy**
Opis: Odprawa musi zostać zakończona minimum 45 minut przed planowanym odlotem.

* **RB-PIO004: Generowanie karty pokładowej**
Opis: Karta pokładowa jest generowana automatycznie po pomyślnej odprawie i zawiera niezbędne dane do kontroli i boardingu.

* **RB-PIO005: Zarządzanie bagażem**
Opis: Każdy bagaż rejestrowany musi zostać oznaczony unikalną etykietą zawierającą kod kreskowy/QR.

* **OS-PIO001: Limit bagażu rejestrowanego**
Opis: Maksymalna waga pojedynczego bagażu rejestrowanego nie może przekraczać 32 kg, a łączna waga zgodna z limitami taryfy.

* **OS-PIO002: Walidacja miejsc w samolocie**
Opis: System nie może przypisać tego samego miejsca dwóm różnym pasażerom na tym samym locie.

* **OS-PIO003: Sekwencja kontroli bezpieczeństwa**
Opis: Pasażer może przejść przez kontrolę bezpieczeństwa tylko po pomyślnej odprawie i z ważną kartą pokładową.

* **OS-PIO004: Warunki boardingu**
Opis: Pasażer może wejść na pokład tylko po pomyślnym przejściu wszystkich etapów kontroli (odprawa, kontrola bezpieczeństwa).

* **OS-PIO005: Zamknięcie lotu**
Opis: Boarding musi zostać zakończony minimum 20 minut przed planowanym odlotem samolotu.

* **OS-PIO006: Integracja statusu lotu**
Opis: Odprawa i boarding są blokowane dla lotów o statusie "ODWOŁANY", "W_POWIETRZU" lub "WYLĄDOWAŁ".

* **OS-PIO008: Limit czasowy modyfikacji**
Opis: Zmiany w rezerwacji mogą być dokonywane tylko do 4 godzin przed odlotem.


#### Loty i harmonogramy

* **RB-LIH001: Walidacja numeru lotu**
Opis: Numer lotu musi być unikalny w systemie i składać się z kodu linii lotniczej (2 znaki) oraz numeru (1-4 cyfry).

* **RB-LIH002: Przydział zasobów infrastrukturalnych**
Opis: Lot może zostać przypisany do pasa startowego, bramki i stanowiska postojowego tylko jeśli są one dostępne w planowanym czasie operacji.

* **RB-LIH003: Zarządzanie slotami czasowymi**
Opis: Lot musi posiadać potwierdzony slot czasowy od organów ATC przed wpisaniem go do harmonogramu.

* **RB-LIH004: Aktualizacja statusu lotu**
Opis: Status lotu jest automatycznie aktualizowany na "WYSTARTOWAŁ" po otrzymaniu potwierdzenia z systemu ATC.

* **RB-LIH005: Zarządzanie opóźnieniami**
Opis: W przypadku opóźnienia przekraczającego 15 minut, system automatycznie powiadamia pasażerów i zespoły obsługi.

* **RB-LIH006: Tworzenie harmonogramu**
Opis: Nowy lot może zostać dodany do harmonogramu tylko przez uprawnionego planistę lotów.

* **RB-LIH007: Weryfikacja konfliktów**
Opis: System automatycznie weryfikuje konflikty w harmonogramie i alertuje planistę o kolizjach czasowych.

* **OS-LIH001: Konflikty w harmonogramie**
Opis: System nie pozwala na przydzielenie tego samego pasa startowego lub bramki dwóm różnym lotom w nakładających się przedziałach czasowych.

* **OS-LIH002: Walidacja łańcucha przesiadkowego**
Opis: Minimalny czas pomiędzy kolejnymi lotami tego samego samolotu musi wynosić co najmniej 45 minut (standardowy turnaround).

* **OS-LIH003: Przypisanie załogi**
Opis: Załoga musi być przypisana do lotu na minimum 2 godziny przed planowanym odlotem.

* **OS-LIH004: Zmiana zasobów infrastrukturalnych**
Opis: Zmiana przydzielonej bramki lub pasa startowego jest możliwa tylko do 30 minut przed planowaną operacją.

* **OS-LIH005: Integracja z FIDS**
Opis: System automatycznie publikuje informacje o locie na tablicach FIDS gdy status lotu zmieni się na "OPÓŹNIONY", "ODWOŁANY" lub "WYSTARTOWAŁ".

* **OS-LIH006: Zarządzanie powiadomieniami**
Opis: Powiadomienia o zmianach są wysyłane automatycznie do pasażerów gdy opóźnienie przekracza 30 minut lub następuje zmiana bramki.

* **OS-LIH007: Walidacja danych lotu**
Opis: Lot musi mieć zdefiniowane: numer lotu, trasę, samolot, slot czasowy i linię lotniczą przed dodaniem do harmonogramu.

* **OS-LIH008: Monitorowanie stanu floty**
Opis: System śledzi pozycję samolotów i automatycznie aktualizuje czasy przylotu/odlotu w oparciu o dane z systemu śledzenia floty.

* **OS-LIH009: Zarządzanie alertami**
Opis: System wyświetla alerty o krytycznych sytuacjach gdy czas łańcucha przesiadkowego przekracza 90 minut lub brakuje przydzielonych zasobów.

* **OS-LIH010: Blokada modyfikacji**
Opis: Modyfikacje harmonogramu są blokowane dla lotów, które mają status "WYSTARTOWAŁ" lub "WYLĄDOWAŁ".

* **OS-LIH011: Ręczna korekta statusu**
Opis: Koordynator lotów może ręcznie zmienić status lotu na "OPÓŹNIONY" lub "ODWOŁANY" tylko z obowiązkowym podaniem przyczyny.

* **OS-LIH012: Sekwencja operacji lotu**
Opis: Status lotu może zmieniać się tylko zgodnie z sekwencją: ZAPLANOWANY → OPÓŹNIONY/WYSTARTOWAŁ → WYLĄDOWAŁ.

#### Bezpieczeństwo i incydenty

* **RB-BII001: Obowiązkowa kategoryzacja i priorytetyzacja incydentu**
Każdy incydent musi mieć przypisaną kategorię (np. medyczny, techniczny) oraz priorytet (np. Krytyczny, Wysoki).

* **RB-BII002: Przypisanie incydentu do zespołu**
Dyspozytor musi przypisać incydent o statusie "Nowy" do zespołu o kwalifikacjach zgodnych z kategorią zdarzenia.

* **RB-BII003: Wymóg raportu zamknięcia**
Incydent może zostać zamknięty dopiero po wypełnieniu przez zespół interwencyjny raportu z podjętych działań.

* **RB-BII004: Eskalacja incydentu**
Incydent o priorytecie "Wysokim" lub "Krytycznym" musi zostać eskalowany do Managera ds. Bezpieczeństwa, jeśli jego status nie zmieni się na "W toku" w ciągu 10 minut od przydzielenia.

* **RB-BII005: Aktywacja procedury komunikacji kryzysowej**
Incydent o priorytecie "Krytycznym" (np. pożar, zagrożenie bombowe) wymaga od Dyspozytora natychmiastowego uruchomienia procedury komunikacji kryzysowej.

* **OS-BII001: Automatyczne generowanie incydentów krytycznych**
Alarm z zintegrowanego systemu bezpieczeństwa (np. PPOŻ, KD) musi automatycznie wygenerować w systemie incydent o priorytecie "Krytyczny".

* **OS-BII002: Walidacja cyklu życia incydentu**
System musi wymuszać zmianę statusu incydentu zgodnie z sekwencją:  
**Nowy → Przydzielony → W toku → Rozwiązany → Zamknięty**.

* **OS-BII003: Wymagalność kluczowych pól**
Zapisanie nowego incydentu wymaga podania jego **typu**, **lokalizacji** oraz **źródła zgłoszenia**.

* **OS-BII004: Niezmienność zarchiwizowanych danych**
Dane incydentu o statusie "Zamknięty" stają się niezmienialne (**read-only**).

* **OS-BII005: Automatyczne powiadomienia o przydzieleniu zadania**
Przypisanie incydentu do zespołu musi automatycznie wyzwolić wysłanie powiadomienia do jego członków, zawierającego **ID incydentu**, **lokalizację** i **priorytet**.

* **OS-BII006: Ograniczenia uprawnień do modyfikacji**
Tylko role **Dyspozytor centrum bezpieczeństwa** lub **Administrator systemu** mogą tworzyć i przydzielać incydenty.  
Rola **Członek zespołu interwencyjnego** może jedynie aktualizować status własnych zadań.

* **OS-BII007: Automatyczna integracja z innymi domenami**
Incydent dotyczący zasobu lotniskowego (np. bramka, pas startowy) musi automatycznie generować powiadomienie do powiązanych domen (np. **Loty i harmonogramy**).

#### Obsługa naziemna i zasoby

* **RB-ONZ001: Przydział personelu do zadań**
Opis: Pracownik może zostać przypisany do zadania tylko, jeśli posiada wymagane kwalifikacje i uprawnienia oraz jest dostępny w danej zmianie.

* **RB-ONZ002: Przydział sprzętu i pojazdów**
Opis: Sprzęt lub pojazd mogą zostać przypisane do zadania tylko, jeśli mają status „SPRAWNY” i nie są obecnie zarezerwowane ani w przeglądzie.

* **RB-ONZ003: Zakończenie zadania**
Opis: Zadanie może zostać oznaczone jako zakończone dopiero po potwierdzeniu wykonania przez przypisanego pracownika i weryfikacji przez koordynatora.

* **RB-ONZ004: Raportowanie incydentów**
Opis: Każdy incydent lub awaria musi zawierać identyfikator zadania lub zasobu, opis zdarzenia, datę, godzinę i dane osoby zgłaszającej.

* **RB-ONZ005: Zarządzanie zmianami**
Opis: Harmonogram zmian personelu musi być zdefiniowany z co najmniej 24-godzinnym wyprzedzeniem. Modyfikacje wymagają zatwierdzenia przez kierownika zmiany.

* **RB-ONZ006: Raportowanie stanu zasobów**
Opis: Stan techniczny sprzętu i pojazdów musi być aktualizowany minimum raz na 24 godziny. Brak aktualizacji zmienia status zasobu na „DO WERYFIKACJI”.

* **RB-ONZ007: Powiązanie z lotem**
Opis: Zadania operacyjne (np. tankowanie, sprzątanie, załadunek) muszą być powiązane z konkretnym lotem. Wyjątek stanowią testy sprzętu lub szkolenia.

* **RB-ONZ008: Uprawnienia do modyfikacji zasobów**
Opis: Zmiana statusu sprzętu lub pojazdu (np. z „SPRAWNY” na „NIEDOSTĘPNY”) może być wykonana tylko przez technika utrzymania lub kierownika zasobów.

* **RB-ONZ009: Raporty operacyjne**
Opis: System generuje dzienne raporty dotyczące wykorzystania personelu, sprzętu i pojazdów. Raporty są archiwizowane przez minimum 12 miesięcy.

* **RB-ONZ010: Zgłaszanie awarii krytycznych**
Opis: Awaria sprzętu kluczowego (np. cysterny paliwowej, pojazdu transportowego) automatycznie generuje komunikat do domeny „Zarządzanie bezpieczeństwem i incydentami”.

* **OS-ONZ001: Weryfikacja dostępności personelu**
Opis: System nie pozwala przypisać pracownika do więcej niż jednego zadania w tym samym przedziale czasowym.

* **OS-ONZ002: Weryfikacja dostępności sprzętu**
Opis: System blokuje przypisanie zasobu technicznego, który jest w trakcie konserwacji, rezerwacji lub oznaczony jako „NIEDOSTĘPNY”.

* **OS-ONZ003: Kolejność statusów zadania**
Opis: Status zadania może zmieniać się tylko w kolejności: „ZAPLANOWANE” → „W TOKU” → „ZAKOŃCZONE”.

* **OS-ONZ004: Blokada usuwania aktywnych zasobów**
Opis: System nie pozwala usunąć pracownika, pojazdu lub sprzętu przypisanego do aktywnych lub niezakończonych zadań.

* **OS-ONZ005: Integracja z lotami i harmonogramami**
Opis: Zadania powiązane z lotem nie mogą rozpocząć się, dopóki lot nie ma statusu „PRZYGOTOWANY DO OBSŁUGI” w domenie „Loty i harmonogramy”.

* **OS-ONZ006: Integracja z bezpieczeństwem**
Opis: W przypadku aktywnego incydentu lub ograniczenia strefy, system blokuje przydzielanie zadań do lokalizacji objętych ryzykiem.

* **OS-ONZ007: Walidacja kwalifikacji personelu**
Opis: System automatycznie sprawdza zgodność uprawnień pracownika z wymaganiami zadania (np. uprawnienie do obsługi pojazdu paliwowego).

### 5. Prototypy interfejsu

---

#### Pasażerowie i odprawy - Boarding


<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Boarding/Boarding_Strona_główna.png" 
    width="60%">
<br>
<em>Strona główna po zalogowaniu pracownika lotniska.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Boarding/Boarding_lista_pasażerów.png" 
    width="60%">
<br>
<em>Po naciśnięciu przycisku "Rozpocznij boarding" pracownik widzi listę pasażerów, ich status oraz szczegóły lotu.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Boarding/Boarding_szczegóły_pasażera.png" 
    width="60%">
<br>
<em>Po zeskanowaniu karty pokładowej pasażera, pracownik widzi jego dane, które może zatwierdzić po zweryfikowaniu.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Boarding/Boarding_nieprawidłowy_lot.png" 
    width="60%">
<br>
<em>Jeżeli zeskanowana karta pokładowa dotyczy innego lotu, to w szczegółach pasażera wyświetlony zostanie odpowiedni komunikat.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Boarding/Boarding_wprowadzenie_karty_pokładowej.png" 
    width="60%">
<br>
<em>W przypadku niedziałającego skanera, pracownik po naciśnięciu "Wyszukaj kartę pokładową" może wprowadzić numer karty pokładowej, której nie udało się zeskanować.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Boarding/Boarding_ostrzeżenie.png" 
    width="60%">
<br>
<em>Po naciśnięciu "Zakończ boarding" w przypadku jeżeli nie wszyscy pasażerowie przeszli boarding, zostanie wyświetlony odpowiedni komunikat.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Boarding/Boarding_pomyślnie_zakończono.png" 
    width="60%">
<br>
<em>Na koniec zostanie wyświetlony komunikat o pomyślnym zakończeniu boardingu.</em>
</p>

---

#### Pasażerowie i odprawy - Odprawa pasażera

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_Strona_główna.png" 
    width="60%">
<br>
<em>Strona główna po zalogowaniu pasażera</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_Rezerwacje_pasażera.png" 
    width="60%">
<br>
<em>Po naciśnięciu przycisku "Moje rezerwacje" pasażer widzi wszystkie jego rezerwacje: zarówno poprzednie, jak i nadchodzące.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_wybór_pasażerów.png" 
    width="60%">
<br>
<em>Po naciśnięciu linku "check-in" przy danej rezerwacji pasażer zostaje przekierowany do widoku, gdzie wybiera, których pasażerów chce odprawić.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_dodanie_dokumentu_tożsamości.png" 
    width="60%">
<br>
<em>Po wybraniu pasażerów do odprawy i naciśnięciu przycisku "Przejdź dalej" pasażer zostaje przekierowany do strony, na której zostaje poproszony o uzupełnienie informacji dotyczących dowodu tożsamości każdego z odprawianych pasażerów.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_dodanie_dokumentu_tożsamości-niepoprawne_dane.png" 
    width="60%">
<br>
<em>Jeżeli pasażer nie uzupełni wszystkich danych to zostanie wyświetlony komunikat o niepoprawnych danych.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_wybór_bagażu.png" 
    width="60%">
<br>
<em>Po poprawnym uzupełnieniu danych i naciśnięciu "Przejdź dalej" pasażer zostaje przekierowany do widoku, gdzie może wybrać rodzaj bagażu, jaki ze sobą zabiera każdy z pasażerów.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_wybór_miejsca.png" 
    width="60%">
<br>
<em>Po wybraniu bagażu, pasażer może wybrać miejsca w samolocie.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_wybór_metody_płatności.png" 
    width="60%">
<br>
<em>Jeżeli pasażer wybrał miejsca i zrezygnował z losowego przydziału to wybiera metodę płatności.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Pasażerowie_i_odprawy/Odprawa_pasażera/Odprawa_pobranie_kart_pokładowych.png" 
    width="60%">
<br>
<em>Na koniec pasażer otrzymuje karty pokładowe z opcją pobrania i wydruku.</em>
</p>

---

#### Loty i harmonogramy - Dodanie lotu

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Loty_i_harmonogramy/Dodawanie_lotu/1.png" 
    width="60%">
<br>
<em>Formularz do dodania lotu.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Loty_i_harmonogramy/Dodawanie_lotu/2.png" 
    width="60%">
<br>
<em>Formularz w przypadku gdy podane dane się nie zgadzają.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Loty_i_harmonogramy/Dodawanie_lotu/3.png" 
    width="60%">
<br>
<em>Formularz w przypadku poprawnych danych.</em>
</p>

---

#### Loty i harmonogramy - Zmiana statusu

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Loty_i_harmonogramy/Zmiana_statusu/1.png" 
    width="60%">
<br>
<em>Zmiana statusu na odwołany.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Loty_i_harmonogramy/Zmiana_statusu/2.png" 
    width="60%">
<br>
<em>Zmiana statusu na odwołany z podaniem przyczyny.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Loty_i_harmonogramy/Zmiana_statusu/3.png" 
    width="60%">
<br>
<em>Formularz w przypadku normalnej sytuacji lotu.</em>
</p>

---

#### Bezpieczeństwo i incydenty - Raportowanie incydentu

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Raportowanie_incydentu/1.png" 
    width="20%">
<br>
<em>Na urządzeniu mobilnym pojawia się powiadomienie o przypisaniu do incydentu, wysyłane co 5 sekund.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Raportowanie_incydentu/2.png" 
    width="20%">
<br>
<em>Po kliknięciu w powiadomienie, Członek zespołu interwencyjnego widzi incydent, jego szczegóły oraz pola do aktualizacji.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Raportowanie_incydentu/3.png" 
    width="20%">
<br>
<em>Po zmianie statusu incydentu, użytkownik otrzymuje informację o pomyślnym działaniu.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Raportowanie_incydentu/4.png" 
    width="20%">
<br>
<em>Członek zespołu interwencyjnego aktualizuje dziennik akcji poprzez wypełnienie pola tekstowego.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Raportowanie_incydentu/5.png" 
    width="20%">
<br>
<em>Po dodaniu akcji w dzienniku, Członek zespołu interwencyjnego otrzymuje potwierdzenie.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Raportowanie_incydentu/6.png" 
    width="20%">
<br>
<em>Po kliknięciu przycisku 'Zgłoś zakończenie incydentu', wyświetla się modal z potwierdzeniem działania.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Raportowanie_incydentu/7.png" 
    width="20%">
<br>
<em>Po zatwierdzeniu zakończenia incydentu, Członek zespołu interwencyjnego otrzymuje komunikat i jest przenoszony do panelu głównego aplikacji.</em>
</p>

---

#### Bezpieczeństwo i incydenty - Zarządzanie incydentami

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Panel_incydentow/1.png" 
    width="60%">
<br>
<em>Centrum zarządzania incydentami lotniskowymi, po lewej stronie znajdują się aktywne incydenty zawierające najważniejsze informacje wraz z statusem. Pośrodku mapa lotniska wraz z pinezkami na mapie wskazującymi na incydenty. Po prawej alerty pochodzące z czujników/ innych źródeł, potencjalne źródło incydentów. Niżej szczegóły wybranego incydentu.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Panel_incydentow/2.png" 
    width="60%">
<br>
<em>Gdy alert wskazuje na ryzyko, po kliknięciu przycisku 'Stwórz incydent', pojawia się modal tworzenia incydentu, należy wypełnić pola wszystkie pola formularza.</em>
</p>

<br>

<p align="center">
<img 
    src="/Etap%20I/Prototypy_interfejsu/Bezpieczenstwo_i_incydenty/Panel_incydentow/3.png" 
    width="60%">
<br>
<em>Po stworzeniu incydentu, pojawia się on w panelu aktywnych incydentów i mapie, znika z pola 'Nowe alerty', pokazuje się również potwierdzenie dodania incydentu.</em>
</p>