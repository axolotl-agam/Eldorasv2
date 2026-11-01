# Eldoras V2 — Sunucu Mimarisi ve MMORPG Tasarım Taslağı

> Durum: TASARIM TASLAĞI. Bu belge çalışan sunucu, indirilmiş eklenti veya test edilmiş kurulum anlamına gelmez. Eklenti sürümleri kurulmadan önce Minecraft/Paper sürümüyle tek tek doğrulanacaktır.

## 1. Hedef ve temel mimari

- Oyun: Minecraft Java Edition, Paper sunucuları; oyuncu ağı Velocity arkasında.
- Ağ: 1 Velocity proxy + 7 ayrı Paper oyun sunucusu (toplam 8 süreç).
- Her Paper sunucusu yalnızca kendi bölgesinin dünyalarını, yaratıklarını, NPC'lerini ve etkinliklerini çalıştırır.
- Ortak veri: MySQL/MariaDB. Önerilen ek servis: Redis (HuskSync mesajlaşma/cache katmanı için).
- Proxy sunucuları internete açık tek giriş noktasıdır. Paper sunucularının portları güvenlik duvarıyla dış erişime kapatılır; yalnız proxy ve yönetim erişimi izinlidir. Modern forwarding secret kullanılır.
- /rtp <bolge> komutu rastgele koordinata değil, hedef bölgenin güvenli başlangıç/ışınlanma noktasına geçiş yapar. Komut oyuncuyu Velocity üzerinden hedef Paper sunucusuna taşır; hedefte güvenli konum kontrolü yapılır.
- Oyuncu verisi: envanter, ender chest, XP/level, sağlık/açlık, konum ve oyun modu için tek bir yetkili senkronizasyon stratejisi belirlenir. Para, sınıf, beceri, görev, pet, lonca ve evlilik verileri ayrı kalıcı MMORPG tablolarında tutulur. Aynı veriyi iki eklentinin yönetmesine izin verilmez.

## 2. Yedi bölge

| Sıra | Bölge | Önerilen seviye | Başlangıç şehri | Tema ve yaratık örnekleri |
|---|---|---:|---|---|
| 1 | Valoria | 1–20 | Gümüşhisar | Yeşil ovalar; kurt, haydut, goblin, orman ayısı |
| 2 | Nordia | 21–40 | Karakış | Karlı dağlar; buz kurdu, yeti, buz golemi |
| 3 | Krallık I — Aetherion | 41–60 | Gökburç | Yüksek yaylalar/uçan adalar; harpi, fırtına ruhu, gök wyvern |
| 4 | Krallık II — Thalassor | 61–80 | Mercanliman | Kıyı ve batık şehir; korsan, deniz yılanı, kabuklu dev |
| 5 | Krallık III — Varkhazan | 81–100 | Kızılhisar | Lanetli orman/volkan; kan yarasası, lanetli şövalye, iblis |
| 6 | Mechanoria | 101–110 | Dişlikent | Mekanik çoraklık; hurda golem, muhafız robot, mekanik avcı |
| 7 | Eryndor | 111–120 | Yıldızkale | Kozmik/son oyun; yıldız muhafızı, kozmik ejderha, kadim boss |

Bölgeler arasında seviye kapısı, görev zinciri ve geçiş NPC'si bulunur. Oyuncu eski bölgelere geri dönebilir. Her bölgenin ayrı spawn, pazar, demirci, depo, lonca NPC'si ve kale alanı vardır.

## 3. Sistem tasarımı ve önerilen teknoloji

Eklenti seçimi sürüm ve lisans kontrolüne tabidir; aşağıdaki liste kesin indirme/sürüm garantisi değildir.

| Sistem | Önerilen uygulama | Tasarım notu |
|---|---|---|
| Seviye/EXP (120) | MMOCore (uygun sürüm/lisans doğrulaması) veya EldorasCore modülü | EXP tabloları config; mob, quest, dungeon kaynakları; 120 üstü kapalı; seviye başına beceri puanı |
| Sınıf/beceri | MMOCore + MMOItems/MMO skill entegrasyonu veya özel modül | Savaşçı, Ninja, Şaman, Suikastçı; aktif/pasif ağaçlar; sınıf değişimi NPC ve ücretle |
| Eşya/efsun/+9 | MMOItems + özel yükseltme modülü | Eşya kimlikleri sabit; rastgele bonus aralıkları; yükseltme olasılığı ve koruma taşı |
| Yang/Won | Vault uyumlu ekonomi + tek özel ekonomi katmanı | 1 Won = 100.000.000 Yang; veri tipi 64-bit integer/decimal; oyuncu ticareti ve log |
| Lonca | Özel EldorasCore modülü veya sürüm doğrulanmış guild eklentisi | Seviye 40, 200.000 Yang kuruluş; lonca EXP, beceri, savaş ve kasa |
| Kale kuşatması | Özel etkinlik modülü + WorldGuard bölgeleri | Her bölgede kale; takvimli savaş; bölge vergisi üst limitli ve kayıtlı |
| Zindan | BetonDungeon/özel dungeon akışı + MythicMobs boss | Seviye/grup kontrolü, ortak parti girişi, boss aşamaları, haftalık/ günlük kilit |
| Görev | BetonQuest | NPC diyalogları, dallanan zincirler, ödül ve koşullar; yedekleme |
| Parti (8) | MMOCore party özelliği veya özel parti modülü | 8 kişi; seviye farkına göre azalan EXP; lider bonusu sınırlı |
| Evlilik | Özel EldorasCore modülü | Seviye 25+, en fazla 15 seviye farkı; eşe ışınlanma cooldown; küçük ve dengeli bonus |
| Pet | MythicMobs + özel pet ilerleme modülü | Yumurta, seviye, ömür/bakım, beceri slotu; pet bonusları sınırlandırılır |
| Balıkçılık/madencilik | Jobs Reborn veya özel meslek modülü | Mini oyun, beceri seviyesi, kaynak havuzu ve günlük kazanç sınırı |
| Metin taşı | Sürüm/lisansı doğrulanmış Metin eklentisi veya MythicMobs tabanlı özel modül | Dünya/biome spawn havuzu, ortak can, hasar katkısı, ödül tablosu ve anti-farm |

### Kritik uyumluluk notları
- HuskSync, birden fazla Paper sunucusu arasında oyuncu verisi senkronizasyonu için adaydır; resmi proje belgesi MySQL/MariaDB ve Redis dahil bir veri altyapısı gerektirdiğini belirtir. Her veri alanı için sahiplik/çakışma testi yapılmadan canlıya alınmaz.
- BetonQuest, Paper üzerinde gelişmiş görev ve konuşma akışı için güçlü bir adaydır.
- Velocity, proxy katmanıdır; bölge değiştirme komutu tek başına Velocity özelliği değildir. Komutun proxy API/uyumlu eklenti üzerinden yönlendirmesi gerekir.
- WorldGuard koruma içindir; ekonomi, RPG veya bölge sunucuları arası veri senkronizasyonu sağlamaz.
- ShopGUIPlus/Vault tek başına tam Yang/Won muhasebesi veya pazar sistemi değildir.
- DMetin/CrMetin adları için resmi sürüm, 1.21.x uyumu, güncelleme ve lisans doğrulanmadan bağımlılık eklenmez.
- ProMMO 2/VortexRPG/MMOCore arasında karar, gerçek sürüm desteği, satın alma/lisans, API ve birlikte çalışma testinden sonra verilir. Aynı anda birden fazla RPG çekirdeği kullanılmaz.

## 4. Başlangıç denge değerleri (test edilecek)

- Seviye aralığı: 1–120; her seviye +1 beceri puanı.
- Mob EXP: oyuncunun seviyesine yakın yaratık için temel değer; oyuncudan 8+ seviye düşük mob EXP vermez veya çok düşük verir.
- Grup: EXP yalnızca katkı/mesafe/aktiflik koşullarını sağlayan üyeler arasında paylaşılır; 8 kişilik grupta toplam EXP üretimi tek oyuncu farmından daha verimli olmayacak şekilde ölçeklenir.
- Yang kaynakları: mob drop, görev, boss ve NPC satışları günlük hedef bütçeye bağlanır. Oyuncular arası ticaret yeni Yang üretmez.
- Yang giderleri: tamir, yükseltme denemesi, pazar listeleme/komisyonu, lonca kuruluş/bağış, kale başvuru bedeli ve teleport ücretleri.
- Won yalnızca 100.000.000 Yang dönüşümünde oluşur; kesirli Won yoktur. Dönüşüm ve transferler atomik işlem ve log ile yapılır.
- + basma için başlangıç taslağı: +1–+3 yüksek başarı ve yalnız materyal tüketimi; +4–+6 orta başarı ve başarısızlıkta seviye düşmesi; +7–+9 düşük başarı, korumasız başarısızlıkta kırılma riski. Kesin yüzdeler ekonomi simülasyonu ve testten sonra belirlenir.
- Efsunlar eşya türüne göre sınırlı havuzlardan gelir; aynı bonusların üst üste binmesi ve PvP/PvE aşırılığı engellenir.
- Kale vergisi, bölgedeki gerçek oyuncu pazar hacminin küçük bir yüzdesiyle sınırlandırılır; loncaya sınırsız pasif Yang basılmaz.

## 5. Dosya ve servis yerleşimi

```
EldorasV2/
  proxy/
    velocity.toml
    forwarding.secret
    plugins/
  servers/
    valoria/
      paper.jar
      server.properties
      plugins/
      world/
      world_nether/
      world_the_end/
    nordia/
    aetherion/
    thalassor/
    varkhazan/
    mechanoria/
    eryndor/
  shared/
    configs/
    resource-pack/
    backups/
  database/
    schema/
    migrations/
  docs/
  scripts/
```

Her bölge sunucusunun kendi Paper yapılandırması ve dünya dosyaları olur. Paylaşılan yapılandırmalar sürüm kontrolünde tutulur; şifreler, forwarding secret ve veritabanı parolaları GitHub'a yazılmaz. Gerçek sunucu dosyaları, dünyalar, eklenti JAR'ları ve yedekler ayrı teslim paketinde bulunur; Git deposu kaynak/config/dokümantasyon içindir.

## 6. Güvenlik ve işletim

- Proxy forwarding secret zorunlu; Paper portları proxy dışından erişilemez.
- MySQL/Redis sadece özel ağdan erişilir; ayrı kullanıcı/şifre ve en az yetki.
- Her gün veritabanı yedeği; dünyalar için düzenli snapshot; geri yükleme testi.
- Eklentiler yalnız resmi kaynaklardan indirilir; lisans ve checksum kaydı tutulur.
- Sunucu başına RAM/CPU bütçesi, oyuncu başına chunk yükü, entity limiti ve view-distance test edilir.
- 7 Paper + proxy + DB/Redis mimarisi düşük bütçeli tek VPS için ağır olabilir. Canlıya geçmeden önce 10 eşzamanlı oyuncu yük testi ve maliyet hesabı yapılır.
- Her eklenti eklenmeden önce test sunucusunda açılış, komut, veri kaydı, sunucular arası geçiş ve yeniden başlatma testleri yapılır.

## 7. Kurulum sırası

1. Sürüm ve lisans matrisi: Paper, Velocity, Java, tüm eklentiler.
2. MySQL/MariaDB ve Redis kurulumu; yedekleme.
3. Velocity proxy ve güvenli forwarding.
4. Tek bir Paper şablonu; ardından 7 bölge sunucusuna çoğaltma.
5. Sunucular arası geçiş ve HuskSync veri sahipliği testi.
6. Ekonomi ve seviye çekirdeği.
7. Sınıflar, beceriler, eşyalar ve +9 yükseltme.
8. Görevler, partiler, zindanlar, bosslar ve metin taşları.
9. Lonca, kale kuşatması, evlilik, pet ve meslekler.
10. Bölge haritaları, NPC'ler, görsel paket ve sesler.
11. 10 oyuncu yük testi, ekonomi simülasyonu, yedek/geri dönüş testi.
12. Sürüm sabitleme ve tek paket halinde teslim.

## 8. Açık kararlar / varsayımlar

Bu taslakta bölge isimleri ve yaratıklar öneridir. Nihai harita tasarımı, Minecraft sürümü, bütçe/hosting kaynakları, PvP kuralları, eşya kaybı, ticaret ve pazar kuralları oyuncunun sonraki istekleriyle kesinleştirilecektir.
