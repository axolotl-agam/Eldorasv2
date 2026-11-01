# Eldoras V2

Minecraft Java MMORPG sunucu projesi. Hedef: Paper 1.21.4 + Java 21 ve Velocity proxy.

## Doğrulanmış kapsam
Bu depo şu anda geliştirme kaynakları ve kurulum şablonları içerir; hazır, test edilmiş bir MMORPG sunucusu paketi değildir.

- `plugins/EldorasCore`: seviye/EXP, sınıf seçimi, temel beceriler, Yang ve basit demirci prototipi.
- `database/schema`: MariaDB başlangıç şeması. EldorasCore henüz bu veritabanına bağlanmaz.
- `infra`: MariaDB ve Redis için Docker Compose şablonu.
- `proxy`: Velocity örnek yapılandırması.
- `servers/regions.yml`: yedi bölgenin isim ve seviye aralıkları.
- `scripts/create-server-folders.ps1`: yedi Paper klasörü ve başlatma dosyası oluşturur.

## Çalıştırma ön koşulları
- Java 21.
- Her bölge için Paper 1.21.4 sunucu JAR'ı.
- Velocity JAR'ı.
- Üçüncü taraf eklentilerin lisanslı/uyumlu JAR'ları.
- DB/Redis için Docker Compose.

## Güvenli kurulum notları
1. `infra/.env.example` dosyasını `infra/.env` olarak kopyalayın; tüm örnek parolaları uzun ve rastgele değerlerle değiştirin.
2. DB/Redis'i yalnızca özel ağda çalıştırın; internete port açmayın.
3. Velocity için güçlü, rastgele `forwarding.secret` üretin.
4. Paper backend'lerinde online-mode kapalı olacaksa Velocity modern forwarding ve aynı secret Paper'ın desteklediği forwarding ayarlarında etkinleştirilmelidir. Backend portlarını genel internete açmayın.
5. Her Paper JAR'ını ilgili `servers/<region>` klasörüne `paper.jar` adıyla yerleştirin.
6. `scripts/create-server-folders.ps1` yalnızca klasör ve temel properties dosyalarını üretir; Paper indirmez, plugin yüklemez, dünyaları oluşturmaz.

## Bilinen tamamlanmamış işler
Paylaşımlı DB entegrasyonu, Velocity geçiş eklentisi, yedi gerçek dünya, görev/NPC, parti, zindan, lonca/kale, evlilik, pet, meslek, metin taşı, tam eşya/efsun/üretim, çapraz sunucu envanter senkronizasyonu ve uçtan uca testler tamamlanmamıştır.
