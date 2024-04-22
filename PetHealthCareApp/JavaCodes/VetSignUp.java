package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityVetSignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VetSignUp extends AppCompatActivity {

    private ActivityVetSignUpBinding binding;

    private String [] cities = new String[]{"Select","Adana", "Adıyaman" , "Afyonkarahisar" , "Ağrı" , "Aksaray" , "Amasya" , "Ankara" , "Antalya" , "Ardahan" , "Artvin" , "Aydın" , "Balıkesir" , "Bartın" , "Batman" , "Bayburt" , "Bilecik" , "Bingöl" , "Bitlis" , "Bolu" , "Burdur" , "Bursa" , "Çanakkale" , "Çankırı" , "Çorum" , "Denizli" , "Diyarbakır" , "Düzce" , "Edirne" , "Elazığ" , "Erzincan" , "Erzurum" , "Eskişehir" , "Gaziantep" , "Giresun" , "Gümüşhane" , "Hakkari" , "Hatay" , "Iğdır" , "Isparta" , "İstanbul" , "İzmir" , "Kahramanmaraş" , "Karabük" , "Karaman" , "Kars" , "Kastamonu" , "Kayseri" , "Kırıkkale" , "Kırklareli" , "Kırşehir" , "Kilis" , "Kocaeli" , "Konya" , "Kütahya" , "Malatya" , "Manisa" , "Mardin" , "Mersin" , "Muğla" , "Muş" , "Nevşehir" , "Niğde" , "Ordu" , "Osmaniye" , "Rize" , "Sakarya" , "Samsun" , "Siirt" , "Sinop" , "Sivas" , "Şanlıurfa" , "Şırnak" , "Tekirdağ" , "Tokat" , "Trabzon" , "Tunceli" , "Uşak" , "Van" , "Yalova" , "Yozgat" , "Zonguldak"};
    private String [] Adana = new String[]{"Select","Aladağ", "Ceyhan", "Çukurova", "Feke", "İmamoğlu", "Karaisalı", "Karataş", "Kozan", "Pozantı", "Saimbeyli", "Sarıçam", "Seyhan", "Tufanbeyli", "Yumurtalık", "Yüreğir"};
    private String [] Adıyaman = new String[]{"Select","Merkez", "Besni", "Çelikhan", "Gerger", "Gölbaşı", "Kahta", "Samsat", "Sincik","Tut"};
    private String [] Afyonkarahisar = new String[]{"Select","Merkez", "Başmakçı", "Bayat", "Bolvadin", "Çay", "Çobanlar", "Dazkırı", "Dinar", "Emirdağ", "Evciler", "Hocalar", "İhsaniye", "İscehisar", "Kızılören", "Sandıklı", "Sinanpaşa", "Şuhut","Sultandağı"};
    private String [] Ağrı = new String[]{"Select","Doğubeyazıt", "Eleşkirt", "Hamur", "Diyadin", "Patnos", "Taşlıçay","Tutak"};
    private String [] Aksaray = new String[]{"Select","Ağaçören","Eskil","Gülağaç","Güzelyurt","Merkez","Ortaköy" ,"Sarıyahşi" ,"Sultanhanı"};
    private String [] Amasya = new String[]{"Select","Merkez", "Göynücek", "Gümüşhacıköy", "Hamamözü", "Merzifon", "Suluova", "Taşova"};
    private String [] Ankara = new String[]{"Select","Akyurt", "Altındağ", "Ayaş", "Balâ", "Beypazarı", "Çamlıdere", "Çankaya", "Çubuk", "Elmadağ", "Etimesgut", "Evren", "Gölbaşı", "Güdül", "Haymana", "Kahramankazan", "Kalecik", "Keçiören", "Kızılcahamam", "Mamak", "Nallıhan", "Polatlı", "Pursaklar", "Sincan", "Şereflikoçhisar","Yenimahalle"};
    private String [] Antalya = new String[]{"Select","Akseki", "Alanya", "Elmalı", "Finike", "Gazipaşa", "Gündoğmuş", "İbradı", "Demre", "Kaş", "Kemer", "Korkuteli", "Kumluca", "Manavgat", "Serik", "Muratpaşa", "Konyaaltı", "Aksu", "Döşemealtı", "Kepez"};
    private String [] Ardahan = new String[]{"Select","Çıldır", "Damal", "Göle", "Hanak", "Posof"};
    private String [] Artvin = new String[]{"Select","Ardanuç", "Arhavi", "Borçka", "Hopa", "Kemalpaşa", "Murgul", "Şavşat", "Yusufeli"};
    private String [] Aydın = new String[]{"Select","Merkez", "Bozdoğan", "Buharkent", "Çine", "Didim", "Germencik", "İncirliova", "Karacasu", "Karpuzlu", "Koçarlı", "Köşk", "Kuşadası", "Kuyucak", "Nazilli", "Söke", "Sultanhisar", "Yenipazar"};
    private String [] Balıkesir = new String[]{"Select","Altıeylül", "Ayvalık", "Balya", "Bandırma", "Bigadiç", "Burhaniye", "Dursunbey", "Edremit", "Erdek", "Gömeç", "Gönen", "Havran", "İvrindi", "Karesi", "Kepsut", "Manyas", "Marmara", "Savaştepe", "Sındırgı", "Susurluk"};
    private String [] Bartın = new String[]{"Select","AMASRA", "KURUCAŞİLE", "MERKEZ", "ULUS"};
    private String [] Batman = new String[]{"Select","Beşiri", "Gercüş", "Hasankeyf", "Kozluk", "Merkez", "Sason"};
    private String [] Bayburt = new String[]{"Select","Merkez", "Aydıntepe", "Demirözü"};
    private String [] Bilecik = new String[]{"Select","Bozüyük", "Söğüt", "Osmaneli", "Merkez", "Gölpazarı", "Pazaryeri", "İnhisar", "Yenipazar"};
    private String [] Bingöl = new String[]{"Select","Merkez", "Adaklı", "Genç", "Karlıova", "Kiğı", "Solhan", "Yayladere","Yedisu"};
    private String [] Bitlis = new String[]{"Select","Hizan", "Mutki", "Ahlat", "Adilcevaz", "Tatvan", "Güroymak"};
    private String [] Bolu = new String[]{"Select","Dörtdivan", "Gerede", "Göynük", "Kıbrıscık", "Mengen", "Mudurnu", "Seben", "Yeniçağa"};
    private String [] Burdur = new String[]{"Select","Ağlasun", "Altınyayla", "Bucak", "Çavdır", "Çeltikçi", "Gölhisar", "Karamanlı", "Kemer", "Tefenni","Yeşilova"};
    private String [] Bursa = new String[]{"Select","Osmangazi", "Nilüfer", "Yıldırım", "Büyükorhan", "Gemlik", "Gürsu", "Harmancık", "İnegöl", "İznik", "Karacabey", "Keles", "Kestel", "Mudanya", "Mustafakemalpaşa", "Orhaneli", "Orhangazi", "Yenişehir"};
    private String [] Çanakkale = new String[]{"Select","Ayvacık", "Bayramiç", "Biga", "Bozcaada", "Çan", "Eceabat", "Ezine", "Gelibolu", "Gökçeada", "Lapseki","Yenice"};
    private String [] Çankırı = new String[]{"Select","Atkaracalar", "Bayramören", "Çerkeş", "Eldivan", "Eskipazar", "Ilgaz", "Kızılırmak", "Korgun", "Kurşunlu", "Orta", "Ovacık", "Şabanözü", "Yapraklı"};
    private String [] Çorum = new String[]{"Select","Merkez", "Alaca", "Bayat", "Boğazkale", "Dodurga", "Kargı", "İskilip", "Laçin", "Oğuzlar", "Mecitözü", "Osmancık", "Ortaköy", "Sungurlu" ,"Uğurludağ"};
    private String [] Denizli = new String[]{"Select","ACIPAYAM", "BABADAĞ", "BAKLAN", "BEKİLLİ", "BEYAĞAÇ", "BOZKURT", "BULDAN", "ÇAL", "ÇAMELİ", "ÇARDAK", "ÇİVRİL", "GÜNEY", "HONAZ", "KALE", "MERKEZEFENDİ", "PAMUKKALE", "SARAYKÖY", "SERİNHİSAR", "TAVAS"};
    private String [] Diyarbakır = new String[]{"Select","Bağlar","Kayapınar", "Sur", "Yenişehir" , "Bismil", "Çermik" ,"Çınar", "Çüngüş", "Dicle", "Eğil", "Ergani", "Hani" ,"Hazro", "Kocaköy", "Kulp" ,"Lice" ,"Silvan"};
    private String [] Düzce = new String[]{"Select","Merkez","Akçakoca","Gölyaka","Kaynaşl","Yığılca","Cumayeri","Çilimli","Gümüşova"};
    private String [] Edirne = new String[]{"Select","Enez", "Havsa", "İpsala", "Keşan", "Lalapaşa", "Meriç", "Süloğlu", "Uzunköprü"};
    private String [] Elazığ = new String[]{"Select","Ağın", "Alacakaya", "Arıcak", "Baskil", "Karakoçan", "Keban", "Kovancılar", "Maden", "Palu", "Sivrice"};
    private String [] Erzincan = new String[]{"Select","Çayırlı", "İliç", "Kemah", "Kemaliye", "Otlukbeli", "Refahiye", "Tercan", "Üzümlü"};
    private String [] Erzurum = new String[]{"Select","Aşkale", "Aziziye", "Çat", "Hınıs", "Horasan", "İspir", "Karayazı", "Karaçoban", "Köprüköy", "Narman", "Palandöken", "Olur", "Oltu", "Pazaryolu", "Pasinler", "Şenkaya", "Tekman", "Tortum", "Uzundere", "Yakutiye"};
    private String [] Eskişehir = new String[]{"Select","Alpu", "Beylikova", "Çifteler", "Günyüzü", "Han", "İnönü", "Mahmudiye", "Mihalgazi", "Mihalıççık", "Tepebaşı", "Sarıcakaya", "Seyitgazi", "Sivrihisar" , "Odunpazarı"};
    private String [] Gaziantep = new String[]{"Select","Merkez", "Araban", "İslahiye", "Karkamış", "Nizip", "Oğuzeli", "Nurdağı", "Şahinbey", "ŞehitKamil", "Yavuzeli"};
    private String [] Giresun = new String[]{"Select","ALUCRA", "BULANCAK", "ÇAMOLUK", "ÇANAKÇI", "DERELİ", "DOĞANKENT", "ESPİYE", "EYNESİL", "GÖRELE", "GÜCE", "KEŞAP", "MERKEZ", "PİRAZİZ", "ŞEBİNKARAHİSAR", "TİREBOLU", "YAĞLIDERE"};
    private String [] Gümüşhane = new String[]{"Select","Kelkit", "Köse", "Kürtün", "Merkez", "Şiran", "Torul"};
    private String [] Hakkari = new String[]{"Select","Çukurca","Derecik","Merkez","Şemdinli", "Yüksekova"};
    private String [] Hatay = new String[]{"Select","Altınözü", "Antakya", "Arsuz", "Belen", "Defne", "Dörtyol", "Erzin", "Hassa", "İskenderun", "Kırıkhan", "Kumlu", "Payas", "Reyhanlı", "Samandağ", "Yayladağı"};
    private String [] Iğdır = new String[]{"Select","Merkez", "Aralık", "Karakoyunlu", "Tuzluca"};
    private String [] Isparta = new String[]{"Select","Aksu", "Atabey", "Eğirdir", "Gelendost", "Gönen", "Keçiborlu", "Senirkent", "Sütçüler", "Şarkikaraağaç", "Uluborlu", "Yalvaç", "Yenişarbademli"};
    private String [] İstanbul = new String[]{"Select","Adalar", "Arnavutköy", "Ataşehir", "Avcılar", "Bağcılar", "Bahçelievler", "Bakırköy", "Başakşehir", "Bayrampaşa", "Beşiktaş", "Beykoz", "Beylikdüzü", "Beyoğlu", "Büyükçekmece", "Çatalca", "Çekmeköy", "Esenler", "Esenyurt", "Eyüpsultan", "Fatih", "Gaziosmanpaşa", "Güngören", "Kadıköy", "Kağıthane", "Kartal", "Küçükçekmece", "Maltepe", "Pendik", "Sancaktepe", "Sarıyer", "Silivri", "Sultanbeyli", "Sultangazi", "Şile", "Şişli", "Tuzla", "Ümraniye", "Üsküdar", "Zeytinburnu"};
    private String [] İzmir = new String[]{"Select","Aliağa", "Balçova", "Bayındır", "Bayraklı", "Bergama", "Beydağ", "Bornova", "Buca", "Çeşme", "Çiğli", "Dikili", "Foça", "Gaziemir", "Güzelbahçe", "Karabağlar", "Karaburun", "Karşıyaka", "Kemalpaşa", "Kınık", "Kiraz", "Konak", "Menderes", "Menemen", "Narlıdere", "Ödemiş", "Seferihisar", "Selçuk", "Tire", "Torbalı", "Urla"};
    private String [] Kahramanmaraş = new String[]{"Select","Elbistan", "Afşin", "Onikişubat", "Dulkadiroğlu", "Pazarcık", "Göksun", "Andırın", "Çağlayancerit", "Türkoğlu", "Nurhak", "Ekinözü"};
    private String [] Karabük = new String[]{"Select","Eflani","Eskipazar","Merkez","Ovacık","Safranbolu","Yenice"};
    private String [] Karaman = new String[]{"Select","Ayrancı", "Başyayla", "Ermenek", "Kazımkarabekir", "Sarıveliler","Merkez"};
    private String [] Kars = new String[]{"Select","Akyaka", "Arpaçay", "Digor", "Kağızman", "Sarıkamış", "Selim", "Susuz"};
    private String [] Kastamonu = new String[]{"Select","Abana", "Cide", "İnebolu", "Tosya", "Daday", "Merkez", "Pınarbaşı", "Çatalzeytin", "Küre", "Taşköprü", "Şenpazar", "Devrekani", "Doğanyurt", "Azdavay", "Ağlı", "Araç", "Bozkurt", "Hanönü", "İhsangazi", "Seydiler"};
    private String [] Kayseri = new String[]{"Select","Akkışla", "Bünyan", "Develi", "Felahiye", "Hacılar", "İncesu", "Kocasinan", "Melikgazi", "Özvatan", "Pınarbaşı", "Sarıoğlan", "Sarız", "Talas", "Tomarza", "Yahyalı", "Yeşilhisar"};
    private String [] Kırıkkale = new String[]{"Select","Bahşili", "Balışeyh", "Çelebi", "Delice", "Karakeçili","Merkez", "Keskin", "Sulakyurt", "Yahşihan"};
    private String [] Kırklareli = new String[]{"Select","Babaeski", "Demirköy", "Kofçaz", "Lüleburgaz", "Pehlivanköy", "Pınarhisar","Vize"};
    private String [] Kırşehir = new String[]{"Select","Merkez","Akçakent", "Akpınar", "Boztepe", "Çiçekdağı", "Kaman", "Mucur"};
    private String [] Kilis = new String[]{"Select","Merkez", "Elbeyli", "Musabeyli" ,"Polateli"};
    private String [] Kocaeli = new String[]{"Select","Başiskele", "Çayırova", "Darıca", "Derince", "Dilovası", "Gebze", "Gölcük", "İzmit", "Kandıra", "Karamürsel", "Kartepe", "Körfez"};
    private String [] Konya = new String[]{"Select","Ahırlı", "Akören", "Akşehir", "Altınekin", "Beyşehir", "Bozkır", "Çeltik", "Cihanbeyli", "Çumra", "Derbent", "Derebucak", "Doğanhisar", "Emirgazi", "Ereğli", "Güneysınır", "Hadim", "Halkapınar", "Hüyük", "Ilgın", "Kadınhanı", "Karatay", "Kulu", "Meram", "Sarayönü", "Selçuklu", "Seydişehir", "Taşkent", "Tuzlukçu", "Yalıhüyük", "Yunak"};
    private String [] Kütahya = new String[]{"Select","Altıntaş", "Aslanapa", "Çavdarhisar", "Domaniç", "Dumlupınar", "Emet", "Gediz", "Hisarcık", "Pazarlar", "Simav", "Şaphane", "Tavşanlı"};
    private String [] Malatya = new String[]{"Select","Akçadağ", "Arapgir", "Arguvan", "Battalgazi", "Darende", "Doğanşehir", "Doğanyol", "Hekimhan", "Kale", "Kuluncak", "Pütürge", "Yazıhan", "Yeşilyurt"};
    private String [] Manisa = new String[]{"Select","Ahmetli", "Akhisar", "Alaşehir", "Demirci", "Gölmarmara", "Gördes", "Kırkağaç", "Köprübaşı", "Kula", "Salihli", "Sarıgöl", "Saruhanlı", "Selendi", "Soma", "Turgutlu", "Şehzadeler", "Yunusemre"};
    private String [] Mardin = new String[]{"Select","Dargeçit", "Derik", "Kızıltepe", "Mazıdağı", "Midyat", "Nusaybin", "Ömerli", "Savur", "Yeşilli"};
    private String [] Mersin = new String[]{"Select", "Akdeniz", "Mezitli", "Yenişehir", "Toroslar","Anamur", "Aydıncık", "Bozyazı", "Silifke", "Tarsus", "Çamlıyayla", "Erdemli", "Gülnar", "Mut"};
    private String [] Muğla = new String[]{"Select","Bodrum", "Dalaman", "Datça", "Fethiye", "Kavaklıdere", "Köyceğiz", "Marmaris", "Menteşe", "Milas", "Ortaca", "Seydikemer", "Ula", "Yatağan"};
    private String [] Muş = new String[]{"Select","Merkez", "Bulanık", "Hasköy", "Korkut", "Malazgirt", "Varto"};
    private String [] Nevşehir = new String[]{"Select","Acıgöl", "Avanos", "Derinkuyu", "Gülşehir", "Hacıbektaş", "Kozaklı","Merkez", "Ürgüp"};
    private String [] Niğde = new String[]{"Select","Merkez","Altunhisar", "Bor", "Çamardı", "Çiftlik", "Ulukışla"};
    private String [] Ordu = new String[]{"Select","Akkuş", "Altınordu", "Aybastı", "Çamaş", "Çatalpınar", "Çaybaşı", "Fatsa", "Gölköy", "Gülyalı", "Gürgentepe", "İkizce", "Kabadüz", "Kabataş", "Korgan", "Kumru", "Mesudiye", "Perşembe", "Ulubey", "Ünye"};
    private String [] Osmaniye = new String[]{"Select","Merkez","Bahçe", "Düziçi", "Hasanbeyli", "Kadirli", "Sumbas", "Toprakkale"};
    private String [] Rize = new String[]{"Select","Merkez","Ardeşen" ,"Çamlıhemşin" ,"Çayeli" ,"Derepazarı" ,"Fındıklı" ,"Güneysu" ,"Hemşin" ,"İkizdere" ,"İyidere" ,"Kalkandere" ,"Pazar"};
    private String [] Sakarya = new String[]{"Select","Adapazarı", "Akyazı", "Arifiye", "Erenler", "Ferizli", "Geyve", "Hendek", "Karapürçek", "Karasu", "Kaynarca", "Kocaali", "Pamukova", "Sapanca", "Serdivan", "Söğütlü", "Taraklı"};
    private String [] Samsun = new String[]{"Select","Alaçam", "Atakum", "Asarcık", "Ayvacık", "Bafra", "Canik", "Havza", "Çarşamba", "İlkadım", "Ladik", "Kavak", "19 Mayıs", "Salıpazarı", "Terme", "Tekkeköy", "Yakakent", "Vezirköprü"};
    private String [] Siirt = new String[]{"Select","Merkez","Baykan","Eruh","Kurtalan", "Pervari", "Şirvan", "Tillo"};
    private String [] Sinop = new String[]{"Select","Merkez","Ayancık", "Boyabat", "Dikmen", "Durağan", "Erfelek", "Gerze", "Saraydüzü", "Türkeli"};
    private String [] Sivas = new String[]{"Select","Akıncılar", "Altınyayla", "Divriği", "Doğanşar", "Geremek", "Gölova", "Gürün", "Hafik", "İmralı", "Kangal", "Koyulhisar", "Suşehri", "Şarkışlı", "Ulaş", "Yıldızeli", "Zara"};
    private String [] Şanlıurfa = new String[]{"Select","Akçakale", "Bozova", "Birecik", "Ceylanpınar", "Eyyübiye", "Haliliye", "Halifeti", "Hilvan", "Harran", "Karaköprü", "Suruç", "Viranşehir", "Siverek"};
    private String [] Şırnak = new String[]{"Select","Merkez", "Beytüşşebap", "Cizre", "Güçlükonak", "İdil", "Silopi", "Uludere"};
    private String [] Tekirdağ = new String[]{"Select","Çerkezköy", "Çorlu", "Ergene", "Hayrabolu", "Kapaklı", "Malkara", "Marmaraereğlisi", "Muratlı", "Saray", "Şarköy", "Süleymanpaşa"};
    private String [] Tokat = new String[]{"Select","Almus", "Artova", "Başçiftlik", "Erbaa", "Niksar", "Pazar", "Reşadiye", "Sulusaray", "Turhal", "Yeşilyurt", "Zile"};
    private String [] Trabzon = new String[]{"Select","Ortahisar", "Akçaabat", "Araklı", "Arsin", "Beşikdüzü", "Çarşıbaşı", "Çaykara", "Dernekpazarı", "Düzköy", "Hayrat", "Köprübaşı", "Maçka", "Of", "Sürmene", "Şalpazarı", "Tonya", "Vakfıkebir", "Yomra"};
    private String [] Tunceli = new String[]{"Select","Çemişgezek", "Hozat", "Mazgirt", "Merkez", "Nazımiye", "Ovacık","Pülümür", "Pertek"};
    private String [] Uşak = new String[]{"Select","Merkez","Banaz", "Eşme", "Karahallı", "Sivaslı", "Ulubey"};
    private String [] Van = new String[]{"Select","Bahçesaray", "Başkale", "Çaldıran", "Çatak", "Gevaş", "Gürpınar", "Erciş", "Edremit", "İpekyolu", "Tuşba", "Saray", "Muradiye", "Özalp"};
    private String [] Yalova = new String[]{"Select","Merkez","Altınova", "Armutlu", "Çınarcık", "Çiftlikköy", "Termal"};
    private String [] Yozgat = new String[]{"Select","Akdağmadeni", "Aydıncık", "Boğazlıyan", "Çandır", "Çayıralan", "Çekerek", "Kadışehri", "Saraykent", "Sarıkaya", "Şefaatli", "Sorgun", "Yenifakılı", "Yerköy","Merkez"};
    private String [] Zonguldak = new String[]{"Select","Merkez","Alaplı", "Çaycuma", "Devrek", "Gökçebey", "Ereğli", "Kilimli", "Kozlu"};

    public Map<String, String[]> map;

    private ArrayAdapter<String> arrayAdapterofCities;
    private ArrayAdapter<String> arrayAdapterOfDistrict;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    private String nameSurname, clinicName,phoneNumber,email,password,address,diplomaNumber,city ="",district="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVetSignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //ARRAY ADAPTER - SPINNER
        arrayAdapterofCities = new ArrayAdapter<String>(VetSignUp.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,cities);
        binding.Cityspinner.setAdapter(arrayAdapterofCities);
        //spinner ı oluşturduk listeyi içine attık
        int initialPosition = binding.Cityspinner.getSelectedItemPosition();
        binding.Cityspinner.setSelection(initialPosition,false);
        //ilk oto seçimi iptal ettik
        binding.Cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position != 0){//ilk uygulamayı açarken işlem yapmaması için bu if i yaptık
                    city = cities[position];
                    getdata();
                    arrayAdapterOfDistrict = new ArrayAdapter<String>(VetSignUp.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, map.get(city));
                    binding.Districtspinner.setAdapter(arrayAdapterOfDistrict);
                    int initialPosition2 = binding.Districtspinner.getSelectedItemPosition();
                    binding.Districtspinner.setSelection(initialPosition2,false);
                    binding.Districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position1, long l) {
                            city = cities[position];
                            if(position1 != 0) {
                                district = map.get(city)[position1];

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            district = "";



                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                city="";
            }
        });

    }

    public void clickedVetComplete(View view){

        nameSurname = binding.enterNameSurnameVet.getText().toString();
        clinicName = binding.enterClinicNameVet.getText().toString();
        phoneNumber = binding.enterPhoneNumberVet.getText().toString();
        email = binding.enterEmailVet.getText().toString();
        password = binding.enterPasswordVet.getText().toString();
        diplomaNumber = binding.enterDiplomaNumber.getText().toString();
        address = binding.enterAddress.getText().toString();
        String city1  = city;
        String district1 = district;
        if(nameSurname.equals("") || clinicName.equals("") || diplomaNumber.equals("") || address.equals("") || district1.equals("") || phoneNumber.equals("") || email.equals("") || password.equals("") || city1.equals("")){
            Toast.makeText(this, "Make sure you write everything completely.", Toast.LENGTH_SHORT).show();
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("NameSurname", nameSurname);
                    userData.put("ClinicName", clinicName);
                    userData.put("DiplomaNumber", diplomaNumber);
                    userData.put("UserEmail", email);
                    userData.put("PhoneNumber", phoneNumber);
                    userData.put("City", city1);
                    userData.put("District", district1);
                    userData.put("Address", address);
                    userData.put("approval","0");

                    firebaseFirestore.collection("VetUsers").document(email).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(VetSignUp.this, "Completed", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VetSignUp.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(VetSignUp.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void getdata(){
        map = new HashMap<String, String[]>();
        map.put("Adana",Adana);
        map.put("Adıyaman",Adıyaman);
        map.put("Afyonkarahisar",Afyonkarahisar);
        map.put("Ağrı",Ağrı);
        map.put("Aksaray",Aksaray);
        map.put("Amasya" ,Amasya);
        map.put( "Ankara" , Ankara);
        map.put("Antalya" , Antalya);
        map.put("Ardahan",Ardahan);
        map.put("Artvin" ,Artvin);
        map.put( "Aydın" ,Aydın);
        map.put( "Balıkesir" ,Balıkesir);
        map.put( "Bartın" , Bartın);
        map.put("Batman" ,Batman);
        map.put( "Bayburt" ,Bayburt);
        map.put( "Bilecik" , Bilecik);
        map.put("Bingöl" , Bingöl);
        map.put("Bitlis" ,Bitlis);
        map.put( "Bolu" , Bolu);
        map.put("Burdur",Burdur);
        map.put( "Bursa" ,  Bursa);
        map.put("Çanakkale" , Çanakkale);
        map.put("Çankırı" , Çankırı);
        map.put("Çorum" , Çorum);
        map.put("Denizli" , Denizli);
        map.put("Diyarbakır" , Diyarbakır);
        map.put("Düzce" ,Düzce);
        map.put( "Edirne" , Edirne);
        map.put("Elazığ" , Elazığ);
        map.put("Erzincan" , Erzincan);
        map.put("Erzurum" , Erzurum);
        map.put("Eskişehir",Eskişehir);
        map.put( "Gaziantep" , Gaziantep);
        map.put("Giresun" , Giresun);
        map.put("Gümüşhane" , Gümüşhane);
        map.put("Hakkari" , Hakkari);
        map.put("Hatay" , Hatay);
        map.put("Iğdır" , Iğdır);
        map.put("Isparta" , Isparta);
        map.put("İstanbul" , İstanbul);
        map.put("İzmir" , İzmir);
        map.put("Kahramanmaraş" , Kahramanmaraş);
        map.put("Karabük",Karabük);
        map.put( "Karaman" , Karaman);
        map.put("Kars" , Kars);
        map.put("Kastamonu" ,Kastamonu);
        map.put( "Kayseri" , Kayseri);
        map.put("Kırıkkale" , Kırıkkale);
        map.put("Kırklareli" , Kırklareli);
        map.put("Kırşehir" , Kırşehir);
        map.put("Kilis" , Kilis);
        map.put("Kocaeli" , Kocaeli);
        map.put("Konya" , Konya);
        map.put("Kütahya" , Kütahya);
        map.put("Malatya",Malatya);
        map.put("Manisa" , Manisa);
        map.put("Mardin" , Mardin);
        map.put("Mersin" , Mersin);
        map.put("Muğla" , Muğla);
        map.put("Muş" , Muş);
        map.put("Nevşehir" , Nevşehir);
        map.put("Niğde" , Niğde);
        map.put("Ordu" , Ordu);
        map.put("Osmaniye" , Osmaniye);
        map.put("Rize" , Rize);
        map.put("Sakarya" , Sakarya);
        map.put("Samsun" , Samsun);
        map.put("Siirt" , Siirt);
        map.put("Sinop" , Sinop);
        map.put("Sivas" , Sivas);
        map.put("Şanlıurfa" , Şanlıurfa);
        map.put("Şırnak" , Şırnak);
        map.put("Tekirdağ" , Tekirdağ);
        map.put("Tokat" , Tokat);
        map.put("Trabzon",Trabzon);
        map.put("Tunceli" , Tunceli);
        map.put("Uşak" , Uşak);
        map.put("Van" , Van);
        map.put("Yalova" , Yalova);
        map.put("Yozgat",Yozgat);
        map.put("Zonguldak",Zonguldak);


    }

}