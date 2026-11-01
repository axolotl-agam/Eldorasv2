# Eldoras V2 Test Planı

Bu liste, statik dosya kontrolü ile gerçek çalışma testlerini birbirinden ayırır.

## Otomatik statik kontroller
- [ ] Zorunlu dosyalar mevcut.
- [ ] Velocity modern forwarding ayarı mevcut.
- [ ] Tam 7 bölge tanımlı.
- [ ] PowerShell yapılandırma denetimi hatasız.

## Derleme ve birim testleri
- [ ] Java 21 ile Gradle build.
- [ ] JUnit testleri.
- [ ] Paper 1.21.4 üzerinde eklenti yükleme.

## Entegrasyon testleri
- [ ] MariaDB bağlantısı ve şema kurulumu.
- [ ] Redis bağlantısı.
- [ ] Velocity -> her backend bağlantısı.
- [ ] Oyuncunun bölgeler arası aktarımı.
- [ ] Oyuncu verisinin farklı backend'de korunması.

## Oyun testleri
- [ ] EXP / seviye / yetenek puanı.
- [ ] Sınıf ve yetenek kullanımı.
- [ ] Yang / Won ve eşya geliştirme.
- [ ] Görev, zindan, lonca, parti, evlilik, pet ve meslek sistemleri.
- [ ] Her bölgenin spawn, warp, mob ve boss içerikleri.

Bir test yalnızca gerçekten çalıştırılıp sonucu kaydedildiğinde tamamlandı sayılır.
