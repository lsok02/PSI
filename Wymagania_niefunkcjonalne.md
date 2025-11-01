### Wymagania niefunkcjonalne

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