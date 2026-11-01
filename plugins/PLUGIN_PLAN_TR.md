# Eldoras V2 — Eklenti Uygulama Planı

> Bu dosya eklenti mimarisini ve kurulum sırasını tanımlar. Henüz JAR dosyaları indirilmiş, sunucuya kurulmuş veya test edilmiş değildir. Her eklenti yalnızca Minecraft Java 1.21.4+ için doğrulanmış sürümü bulunduğunda kullanılacaktır.

## Temel yaklaşım

Tek bir RPG çekirdeği seçilecek; aynı oyuncu verisini yöneten birden fazla eklenti birlikte çalıştırılmayacaktır. Özel ihtiyaçlar için **EldorasCore** adlı kendi Paper eklentimiz geliştirilecek. Velocity tarafındaki sunucu geçişi ayrı bir proxy eklentisiyle yönetilecek.

## Eklenti listesi

| Eklenti / modül | Görev | Durum |
|---|---|---|
| Velocity | 7 bölge sunucusunu tek Java ağı altında birleştirir | Altyapı |
| EldorasProxy | /rtp bölge geçişi, güvenli yönlendirme ve proxy komutları | Geliştirilecek |
| EldorasCore | Seviye 1–120, EXP, sınıflar, beceriler, Yang/Won, +9 geliştirme, efsun, lonca, kale, parti, evlilik, pet ve meslek sistemlerinin özel bağlantı katmanı | Geliştirilecek |
| HuskSync | Paper sunucuları arasında envanter ve seçilmiş oyuncu verilerini senkronize eder | Sürüm/Redis/DB doğrulaması gerekli |
| BetonQuest | NPC görevleri, diyaloglar ve görev zincirleri | 1.21.4+ uyumluluk testi gerekli |
| MythicMobs | Özel yaratıklar, bosslar ve metin taşı karşılaşmaları | Sürüm/lisans doğrulaması gerekli |
| MMOItems | Özel silah, zırh ve aksesuar tanımları | Lisans ve 1.21.4+ doğrulaması gerekli; EldorasCore ile entegrasyon |
| WorldGuard | Şehir, zindan, kale ve güvenli alan koruması | Sürüm doğrulaması gerekli |
| PlaceholderAPI | Eklentiler arası placeholder değişkenleri | Yalnız ihtiyaç varsa |
| Vault | Ekonomi/izin eklentileriyle ortak API | EldorasCore ekonomi modülüyle çakışma testi gerekli |
| LuckPerms | Yetki ve grup yönetimi | Proxy/Paper kurulumu ve ortak veri kaynağı doğrulaması gerekli |

## EldorasCore modülleri

1. LevelModule — seviye, EXP, seviye sınırı ve beceri puanı.
2. ClassModule — Savaşçı, Ninja, Şaman, Suikastçı ve beceri ağaçları.
3. ItemModule — ekipman kimlikleri, efsunlar, +0…+9 geliştirme ve materyal kontrolleri.
4. EconomyModule — Yang/Won, 1 Won = 100.000.000 Yang, işlem kayıtları ve güvenli transfer.
5. QuestBridge — BetonQuest ödüllerini EXP, Yang ve eşya sistemlerine bağlama.
6. PartyModule — 8 kişilik grup ve seviye farkına göre EXP paylaşımı.
7. GuildModule — seviye 40 ve 200.000 Yang kuruluş şartı, lonca seviyesi/becerileri/savaşı.
8. SiegeModule — bölge kaleleri, zamanlı kuşatma ve sınırlandırılmış vergi.
9. DungeonModule — grup/seviye koşulları, boss aşamaları ve giriş kilitleri.
10. MarriageModule — seviye 25+, en fazla 15 seviye farkı, eşe ışınlanma.
11. PetModule — yumurta, seviye, ömür, beceri slotları ve bonus sınırları.
12. ProfessionModule — balıkçılık mini oyunu, madencilik ve cevher işleme.
13. MetinModule — haritada metin taşı spawnı, ortak can, katkı ve ödül hesabı.
14. RegionModule — 7 bölge kimliği, geçiş noktaları, bölgeye özgü kurallar ve sunucu eşlemesi.

## Kurulum ve doğrulama sırası

1. Paper/Velocity ve Java sürüm matrisi sabitlenir.
2. MySQL/MariaDB ve Redis kurulur; yedekleme ve bağlantı güvenliği test edilir.
3. Velocity + 7 Paper sunucusu ayağa kaldırılır.
4. HuskSync ile veri alanı sahipliği belirlenir; iki sunucu arasında geçiş, yeniden başlatma ve çakışma testleri yapılır.
5. LuckPerms ve temel koruma araçları eklenir.
6. EldorasCore iskeleti ve veri şeması oluşturulur.
7. RPG, ekonomi, eşya, görev, yaratık ve diğer modüller sırayla geliştirilip test edilir.
8. Her özellik test sunucusunda doğrulanmadan canlı paket olarak işaretlenmez.

## Önemli

- JAR dosyaları bu commit ile eklenmiş değildir.
- Ücretli eklentiler otomatik satın alınmaz; lisans gerektiren bir ürün seçilirse kullanıcıya açıkça bildirilir.
- Sürüm uyumu doğrulanmayan eklenti, yalnızca aday olarak kalır.
- Gizli anahtarlar, veritabanı parolaları ve forwarding secret Git deposuna konulmaz.