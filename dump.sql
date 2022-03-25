#Specialization
insert into specialization (id, name_of_specialization) values (1, "endokrynolog");
insert into specialization (id, name_of_specialization) values (2, "genetyk");
insert into specialization (id, name_of_specialization) values (3, "gastroenterolog");
insert into specialization (id, name_of_specialization) values (4, "pediatra");
insert into specialization (id, name_of_specialization) values (5, "dietetyk");

#Tag
insert into tag (id, name) values (1, "wcześniak");
insert into tag (id, name) values (2, "poród");
insert into tag (id, name) values (3, "hormonwzrostu");
insert into tag (id, name) values (4, "przyrosty");
insert into tag (id, name) values (5, "ulewanie");

#Role
insert into role (id, name) values (1, "ROLE_ADMIN");
insert into role (id, name) values (2, "ROLE_PUBLISHER");
insert into role (id, name) values (3, "ROLE_USER");

#Articles
insert into articles (id, contents, created, page, priority, title, visible, user_id) values (1,
"Hipotrofię definiuje się jako wewnątrzmaciczne ograniczenie wzrastania płodu. To zbyt mała masa ciała w stosunku do wieku ciążowego. Rozróżnia się następujące rodzaje hipotrofii:
hipotrofia symetryczna - wymiary ciała zmniejszają się proporcjonalnie;
hipotrofia asymetryczna - obwód główki i długość ciała dziecka określa się jako względnie prawidłowe, a obwód brzucha jest zbyt mały. Hipotrofia asymetryczna dotyczy zdecydowanej większości przypadków.
Ostateczne rozpoznanie hipotrofii następuje dopiero po urodzeniu dziecka. Ocenia się parametry anatomiczne i czynnościowe niemowlaka. U noworodka hipotroficznego obserwuje się: wychudzenie; luźną, wysuszoną skórę; słabo rozwiniętą tkankę podskórną; rzadkie włosy; szybko wysychającą pępowinę, wstrzymanie moczu w pierwszych godzinach po urodzeniu, dwa naczynia w sznurze pępowinowym.", 
"2020-06-15", 1, 5,"Hipotrofia",1,2);
insert into articles (id, contents, created, page, priority, title, visible, user_id) values (2,
"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", 
"2020-06-21", 3, 1, "Jasiu",1,2);
insert into articles (id, contents, created, page, priority, title, visible, user_id) values (3,
"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?", 
"2020-06-27", 3, 1,"Kasia",1,4);
insert into articles (id, contents, created, page, priority, title, visible, user_id) values (4,
"Przyczyny hipotrofii są różnorodne, dlatego dzieli się je na grupy. O nieprawidłowościach wzrastania płodu decydują:
czynniki matczyne - przede wszystkim choroby układu krążenia, choroby nerek, czynniki immunologiczne, infekcje, zażywane leki, używki;
czynniki płodowe - ciąża wielopłodowa, wady wrodzone, zakażenia, aberracje chromosomalne;
czynniki łożyskowe - przedwczesne oddzielenie łożyska, nieprawidłowe położenie łożyska, krwiak pozałożyskowy, zapalenie kosmków łożyskowych;
czynniki społeczne - ciężka praca fizyczna, niedożywienie, słaba opieka prenatalna, niski status socjoekonomiczny.", 
"2020-06-27", 2, 1,"Przyczyny hipotrofii",1,2);
insert into articles (id, contents, created, page, priority, title, visible, user_id) values (5,
"Medycyna na obecnym poziomie wiedzy, nie zna skutecznego sposobu na zapobieganie hipotrofii płodu. Wdraża się jedynie leczenie spoczynkowe, które polega na poprawie ukrwienia macicy. Szczególną opiekę zapewnia się matkom cierpiącym na ostre i przewlekłe choroby. Zaleca się także wysokokaloryczne diety wpływające na uzupełnienie niedoborów żywieniowych. W sytuacji zagrożenia życia płodu może zaistnieć konieczność wcześniejszego zakończenia ciąży. Wciąż trwają badania nad skutecznymi sposobami leczenia hipotrofii.
Odpowiednia diagnoza postawiona przed urodzeniem dziecka to klucz do odpowiedniego postępowania z niemowlakiem hipotroficznym. Pielęgnacja powinna opierać się przede wszystkim na przeprowadzeniu dokładnego wywiadu dotyczącego czynników ryzyka. Przedmiotowe badanie noworodka musi zostać wykonane dokładnie. By zapobiec hipotermii, dziecko powinno się umieścić pod promiennikiem ciepła. Następnie należy osuszyć jego skórę i jeśli to konieczne, umieścić w inkubatorze. Opieka nad dzieckiem hipotroficznym zakłada także wyrównanie zaburzeń metabolicznych: hipoglikemii, hiperglikemii, hipokaliemii. Poza tym istotna okazuje się ocena zaburzeń hematologicznych, ale również leczenie infekcji wrodzonych oraz ewentualnych wad.", 
"2020-06-27", 2, 3,"Kasia",1,4);

#tag_articles
insert into tag_article (article_id, tags_id) values (2, 1);
insert into tag_article (article_id, tags_id) values (2, 3);
insert into tag_article (article_id, tags_id) values (3, 1);
insert into tag_article (article_id, tags_id) values (3, 2);
insert into tag_article (article_id, tags_id) values (3, 3);
insert into tag_article (article_id, tags_id) values (4, 1);
insert into tag_article (article_id, tags_id) values (4, 3);
insert into tag_article (article_id, tags_id) values (4, 4);

#Doctor
insert into doctor (id, building_number, city,first_name,last_name,region,street,zip_code) values 
(1, 12, "Warszawa", "Jan", "Kowalski","Mazowsze","Lipna","00-123");
insert into doctor (id, building_number, city,first_name,last_name,region,street,zip_code) values 
(2, 1, "Warszawa", "Anna", "Nowak","Mazowsze","Dębowa","00-153");
insert into doctor (id, building_number, city,first_name,last_name,region,street,zip_code) values 
(3, 32, "Kraków", "Hieronim", "Grecki","Małopolska","Bukowa","31-123");
insert into doctor (id, building_number, city,first_name,last_name,region,street,zip_code) values 
(4, 63, "Kraków", "Ludwika", "Niewiadoma","Małopolska","Świerkowa","31-656");
insert into doctor (id, building_number, city,first_name,last_name,region,street,zip_code) values 
(5, 3, "Poznań", "Michał", "Iksiński","Wielkopolska","Grabowa","60-123");
insert into doctor (id, building_number, city,first_name,last_name,region,street,zip_code) values 
(6, 35, "Wrocław", "Żaneta", "Wrońska","Dolny Śląsk","Jesionowa","50-123");

#Doc_spec
insert into doctor_specialization (doctor_id,specialization_id) values (1,4);
insert into doctor_specialization (doctor_id,specialization_id) values (1,1);
insert into doctor_specialization (doctor_id,specialization_id) values (2,4);
insert into doctor_specialization (doctor_id,specialization_id) values (2,5);
insert into doctor_specialization (doctor_id,specialization_id) values (3,1);
insert into doctor_specialization (doctor_id,specialization_id) values (3,4);
insert into doctor_specialization (doctor_id,specialization_id) values (4,2);
insert into doctor_specialization (doctor_id,specialization_id) values (4,4);
insert into doctor_specialization (doctor_id,specialization_id) values (5,4);
insert into doctor_specialization (doctor_id,specialization_id) values (5,2);
insert into doctor_specialization (doctor_id,specialization_id) values (6,4);
insert into doctor_specialization (doctor_id,specialization_id) values (6,3);
insert into doctor_specialization (doctor_id,specialization_id) values (6,5);
insert into doctor_specialization (doctor_id,specialization_id) values (1,2);

