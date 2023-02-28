#Samenvatting maandag 2022 06 13
Done: context, container en componentdiagrammen gemaakt.

•	opmerkingen: 
- bank account = bankrekening
- user account = klant (client)
- het woord account proberen we te vermijden om misverstanden te voorkomen. 
- SB = Small Business

Notities nav gesprek met opdrachtgever:

•	Opdrachtgever: Nigel Martin, Capital Management Club (CMC). Doel van de app is dat zij bezig zijn met investeringen en ze willen mensen aan zich binden door rekeningen te openen en tevens ook geld te lenen. Ze willen zich vestigen in de UK, maar richten zich om te beginnen op EU. Op termijn banken over de hele wereld.
•	Er moet uiteindelijk een verbinding kunnen worden gelegd met andere banken. Waar nodig rekening houden met mogelijke wisseling in valuta en rekeningnr opbouw. We beginnen echter met euro's en Nederlandse IBAN.
•	De nieuwe userstories zijn inderdaad nice to have.
•	Er is nog geen startproject
•	We maken zelf dummydata aan voor de klantenlijst (voorwaarden zie casus)
•	Een transactie wordt alleen in 2en gesplits als het gaat om een transactie tussen 2 klanten van verschillende banken. Dit gaat van een client bankaccount naar een intern bank account (van de bank) naar de andere banks interne bank account (van die bank) naar het account van de klant van de andere bank. Dit wordt gedaan op de gunstigste manier voor de bank (of direct).
•	men mag negatief staan op rekening: 
	o	Private: max 10.000 in de min, 
	o	SB max 100.000 in de min. Hierover betalen ze 12% rente per maand. De accountmanager moet kunnen zien wie er in de min staan en het is een 
	o	nice to have: om inzichtelijk te krijgen hoe dit zich vergelijkt met vorige maanden.
	o	nice to have: alert voor account manager om melding te krijgen als mensen te veel of te lang rood staan
•	storten van een startbedrag wordt gedaan door de account manager. Hij kan tevens saldos van bankaccounts ophogen of verlagen
•	Een user kan zelf zijn username en wachtwoord kiezen bij aanmaken van account
•	Klanten van de winkelier hebben (om te beginnen) alleen een accoutn bij leenman bank, niet bij andere banken.
•	Een transactie kan worden geweigerd als er onvoldoende saldo is, als een van de rekeningnrs niet klopt. Er is geen minimum of maximum bedrag op de transactie zelf. 
•	Alle naamgeving is in het engels. We houden een lijstje bij van begrippen (zie ook bovenstaand).
•	Voor iedere lening wordt er automatisch een interne tegenrekening gegenereed. Deze staat dan automatisch op een minbedrag voor het geleende bedrag. De rente wordt verrekend op het nog openstaande bedrag, dit is elke maand een vast percentage.
•	Accountmanager moet nieuwe aanvragen voor nieuwe rekeningen (zowel prive als MKB),  toevoegen van account holders aan rekeningen aan bestaande rekeningen, leningen allemaal goedkeuren. 
•	Er zijn 2 accountmanagers per bank: 1 voor Private en 1 voor SB. Zij zijn tevens ook respectievelijk Head Private en Head SB.
•	Er is een softlimiet voor transacties per maand: 
	o	Private: 1 miljoen euro in transacties
	o	SB: 10 miljoen euro in transacties
	o	Indien dit wordt overschreden alert voor de acocuntmanager
•	Een client kan max 10 rekeningen hebben. Dit kan een combi zijn tussen SB en Private accounts.
•	Segment MKB moet worden aangegeven bij het aanvragen van een rekening: er wordt niet gecheckt of MKB rekeningen voor hetzelfde bedrijf zijn. Segmenten van MKB zijn te vinden via KVK of Statline
•	Pinautomaat kan door accountmanager gekoppeld worden (via webapp – code telefonisch doorgeven aan klant) of kan zelf worden gedaan door de klant (dit stond niet in requirements maar is later besproken)
•	Er mogen max 5 rekeninghouders worden toegevoegd aan een bankaccount.
•	De tenaamstelling van bankaccount heeft betrekking op de eigenar(en) van het account. Dit kunnen er dus meerdere zijn. 
•	Accountmanagers moeten volledige CRUD van rekeningen hebben. Ze mogen een account alleen verwijderen als hier geen saldo op staat (negatief of positief!)
•	Onder bewerken van rekening door client valt:
	o	Transacties doen (MUST)
	o	Accountholder toevoegen (MUST)
	o	Pinmachine aanvragen (alleen SB, MUST)
	o	Nice to have: Naam van bank account veranderen, bijv: spaarpotje voor vakantie
•	Client Homepage: Overzicht van SB rekeningen en Private rekeningen in aparte lijstjes op de clientOverview pagina.
•	Voorwaarden voor Pending Approval: deze worden manueel gecheckt door accountmanager. 
	o	Nice to have als er bij het aanmaken een check is voor: geldige email, geldig tel nr (let ook op internationaal), geldige naam, geldig adres, geldig BSN
	o	Button Request Pinmachine, Button Make Transfer komen op de specifieke bankAccountPage te staan (niet op de overview pagina van de client, om fouten te voorkomen)

Prio:
-	Rekeningen aanvragen en aan maken
-	Overschrijvingen plaats kunnen vinden
-	Accountmanagers overzichten
-	Saldo inzien


-	We moeten nog bepalen hoe we de DB van de pinterminal in willen richten (DB textfile, in memory, MySQL?)


#Samenvatting Dinsdag 2022 06 14
DONE: C4 modellen tm Component gedigitaliseerd, Repos aangemaakt, Startproject begin gemaakt
-	We maken een aparte repo voor PinApp en BankApp en Documentatie. Dit wordt gezamenlijk in een groep gezet.

