# Eldoras V2 — Uygulama Durum Kaydı

Bu kayıt, kod deposunda bulunan dosyaları ve doğrulanmış işleri ayırır. Bir dosyanın/şemanın bulunması, özelliğin oyun içinde tamamlandığı anlamına gelmez.

## Mevcut prototip
- EldorasCore: oyuncu YAML kaydı, mob EXP, 120 seviye sınırı, sınıf seçimi, sekiz temel beceri, Yang bakiyesi ve temel demirci yükseltme komutu.
- GitHub Actions: Java 21 / Gradle derleme iş akışı tanımlı; başarılı çalışma ve JAR artefaktı ayrıca doğrulanmalıdır.
- SQL: oyuncu, yetenek, ekonomi işlem günlüğü, lonca, görev, pet ve bölgesel vergi için başlangıç tabloları eklendi.
- Altyapı şablonları: MariaDB/Redis Compose, Velocity örnek ayarı, yedi bölge eşlemesi ve Windows klasör hazırlama betiği eklendi.

## Henüz oyun içinde uygulanmış/test edilmiş sayılmayanlar
Paylaşımlı SQL veri erişimi EldorasCore'a bağlanmadı. Velocity geçiş eklentisi, yedi Paper sunucusu, senkronizasyon, görev/NPC, parti, zindan, lonca/kale, evlilik, pet, meslek, metin taşı, gerçek özel eşya/efsun/üretim, hazır dünya haritaları ve performans/yük testleri tamamlanmış değildir. Üçüncü taraf JAR'lar lisans ve sürüm kontrolüyle ayrıca edinilmelidir.

## Kabul testleri
1. Java 21 ile Gradle build başarılı olmalı.
2. Tek Paper sunucusunda plugin enable, oyuncu kayıt/yükleme, EXP, sınıf, beceri ve ekonomi testleri geçmeli.
3. İki ayrı Paper sunucusunda aynı oyuncu verisi güvenli şekilde taşınmalı.
4. Yedi sunucu + Velocity yeniden başlatma ve bağlantı testi geçmeli.
5. Görev, parti, zindan, lonca, ekonomi, eşya üretimi ve savaş testleri geçmeli.
6. Dünya dosyaları açılmalı, spawn/warp ve bölge seviye kapıları doğrulanmalı.
7. Temiz makinede kurulum dokümanı baştan sona uygulanmalı.
