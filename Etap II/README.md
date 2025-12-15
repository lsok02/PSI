# Wyniki etapu II: Definicja architektury systemu

**Implementacja systemu ERP do zarządzania operacjami i zasobami komercyjnego portu lotniczego**

---

**Skład zespołu:**
+ Michał Klatkowski
+ Dominik Patrzek
+ Aleksandra Pluta
+ Łukasz Sokołowski

**Prowadzący:**
+ dr inż. Marcin Kawalerowicz

---

## 1. Cel
Dokument przedstawia decyzje architektoniczne, ich uzasadnienie oraz strukturę systemu ERP dla portu lotniczego. Dokument definiuje podział na komponenty, sposób ich komunikacji, rozmieszczenie oraz model danych, mając na celu zapewnienie skalowalności, niezawodności i bezpieczeństwa operacji lotniczych.

## 2. Cele i ograniczenia architektoniczne
Wyróżnione zostały następujące cele, które system powinien spełniać:

**Funkcjonalne:**
*   System umożliwia centralne zarządzanie harmonogramem lotów i integrację z kontrolą ruchu (ATC).
*   System umożliwia kompleksową obsługę pasażera: od rezerwacji, przez odprawę, po boarding.
*   System umożliwia zarządzanie zasobami naziemnymi i automatyczne przydzielanie zadań (np. tankowanie).
*   System umożliwia wykrywanie, koordynację i raportowanie incydentów bezpieczeństwa w czasie rzeczywistym.

**Niefunkcjonalne (zgodnie z WNF Etapu I):**
*   **Dostępność (WNF-1):** System musi być dostępny przez min. 99,99% czasu (maks. 52 minuty przestoju rocznie).
*   **Wydajność (WNF-3, WNF-4):** Czas aktualizacji FIDS < 5s; Czas odpowiedzi interfejsu < 2s; Obsługa 5000 transakcji/min.
*   **Bezpieczeństwo (WNF-6, WNF-8):** Autoryzacja oparta na rolach (RBAC), szyfrowanie TLS 1.2+.
*   **Integralność (WNF-7, WNF-9):** Niezmienialne logi audytowe; RPO (utrata danych) < 1 minuta.
*   **Interfejs:** System dostępny jako aplikacja webowa (personel biurowy) i mobilna (personel terenowy).


## 3. Decyzje i ich uzasadnienie
W poniższej tabeli przedstawiono zastosowane taktyki architektoniczne.

| Cel | Sposób osiągnięcia (Taktyki)                                                                                                                                                                                                                                                                               |
| :--- |:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1. Wysoka dostępność (99,99%) i Skalowalność** | **A. Architektura Mikroserwisów:** Podział systemu na niezależne domeny (Loty, Pasażerowie, Zasoby, Bezpieczeństwo) zapobiega kaskadowym awariom.<br>**B. Klaster Kubernetes:** Automatyczne restartowanie i skalowanie instancji serwisów.<br>**C. Load Balancing:** Rozłożenie ruchu pomiędzy instancje. |
| **2. Wydajność i Czas Reakcji** | **A. Event-Driven Architecture:** Asynchroniczna komunikacja między domenami (np. zmiana statusu lotu -> zadanie dla obsługi naziemnej) przy użyciu brokera wiadomości.<br>**B. Caching:** Przechowywanie często odczytywanych danych (statusy lotów, SOP) w pamięci podręcznej.                           |
| **3. Bezpieczeństwo Danych** | **A. Centralny Identity Provider:** Uwierzytelnianie oparte o OAuth2 i tokeny JWT.<br>**B. Segmentacja sieci:** Bazy danych ukryte w sieci prywatnej, dostęp tylko przez API Gateway.                                                                                                                      |
| **4. Integralność i Audytowalność** | **A. Dedykowany Serwis Audytowy:** Asynchroniczny zapis wszystkich operacji modyfikujących do bazy.                                                                                                                                                                                                        |
| **5. Dostępność Mobilna** | **A. API First:** Wszystkie funkcjonalności wystawione przez REST API, konsumowane przez aplikacje Web i Mobile.                                                                                                                                                                                           |


## 8. Widok informacyjny

**8.1 Model informacyjny**

![Diagram - Pasażerowie i odprawy](../Etap%20I/Diagram_klas/Diagram_klas_pasażerowie_i_odprawy.png)


**8.2 Projekt bazy danych**

| Ogólne informacje nt. bazy danych |  |
|---|--|
| SID/Service Name | Pasazerowie-i-odprawy |
| Nazwa serwera | pasazerowie-odprawy-db |
| Port | 5432 |
| Type | Relacyjna – Postgres 16 |
| Kodowanie znaków | UTF-8 |
| Opis | Baza danych do zarządzania pasażerami, rezerwacjami, odprawami, bagażami, kartami pokładowymi oraz procesami boardingu i kontroli bezpieczeństwa. |

| Backup |
|-------|
|       |

| Informacje o schemacie    | |
|---|--|
| Nazwa                     | public |
| Początkowa pojemność      | ≈10MB |
| Przyrost pojemności (rok) | ≈80GB |


![Diagram - Loty i harmonogramy](/Etap%20I/Diagram_klas/Diagram_klas_loty_i_harmonogramy.png)

| Ogólne informacje nt. bazy danych |                                                                                                                                           |
|---|-------------------------------------------------------------------------------------------------------------------------------------------|
| SID/Service Name | Loty-i-harmonogramy                                                                                                                       |
| Nazwa serwera | loty-harmonogramy-db                                                                                                                      |
| Port | 5432                                                                                                                                      |
| Type | Relacyjna – Postgres 16                                                                                                                   |
| Kodowanie znaków | UTF-8                                                                                                                                     |
| Opis | Baza danych do zarządzania harmonogramami lotów, slotami czasowymi, przydziałem zasobów, statusami lotów, danymi samolotów oraz załogami. |

| Backup |
|-------|
|       |

| Informacje o schemacie    |        |
|---|--------|
| Nazwa                     | public |
| Początkowa pojemność      | ≈20MB  |
| Przyrost pojemności (rok) | ≈30GB  |


![Diagram - Bezpieczeństwo i incydenty](/Etap%20I/Diagram_klas/Diagram_klas_bezpieczenstwo_i_incydenty.png)

| Ogólne informacje nt. bazy danych |                                                                                                                                                                                                     |
|---|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| SID/Service Name | Bezpieczenstwo-i-incydenty                                                                                                                                                                          |
| Nazwa serwera | bezpieczenstwo-incydenty-db                                                                                                                                                                         |
| Port | 5432                                                                                                                                                                                                |
| Type | Relacyjna – Postgres 16                                                                                                                                                                             |
| Kodowanie znaków | UTF-8                                                                                                                                                                                               |
| Opis | Baza danych do rejestracji, kategoryzacji, priorytetyzacji i zarządzania incydentami, koordynacji zespołów interwencyjnych, logowania działań, powiadomień oraz integracji z systemami monitoringu. |

| Backup |
|-------|
|       |

| Informacje o schemacie    |        |
|---|--------|
| Nazwa                     | public |
| Początkowa pojemność      | ≈20MB  |
| Przyrost pojemności (rok) | ≈20GB  |



![Diagram - Obsługa naziemna i zasoby](/Etap%20I/Diagram_klas/Diagram_klas_obsluga_naziemna_i_zasoby.png)

| Ogólne informacje nt. bazy danych |                                                                                                                                           |
|---|-------------------------------------------------------------------------------------------------------------------------------------------|
| SID/Service Name | Obsluga-naziemna-i-zasoby                                                                                                                 |
| Nazwa serwera | osluga-naziemna-zasoby-db                                                                                                                 |
| Port | 5432                                                                                                                                      |
| Type | Relacyjna – Postgres 16                                                                                                                   |
| Kodowanie znaków | UTF-8                                                                                                                                     |
| Opis | Baza danych do zarządzania zasobami naziemnymi, przydziałem zadań, planowaniem zmian, kwalifikacjami, logowaniem statusów oraz integracją z systemami lotów, bezpieczeństwa i logistyki.|

| Backup |
|-------|
|       |

| Informacje o schemacie    |        |
|---|--------|
| Nazwa                     | public |
| Początkowa pojemność      | ≈30MB  |
| Przyrost pojemności (rok) | ≈40GB  |