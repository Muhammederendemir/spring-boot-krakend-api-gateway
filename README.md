# Spring Boot + KrakenD API Gateway Projesi

Bu proje, mikroservis mimarisi kullanarak geliştirilmiş bir e-ticaret sistemi örneğidir. Spring Boot ile yazılmış iki ayrı mikroservis (User Service ve Product Service) ve KrakenD API Gateway kullanılarak oluşturulmuştur.

## 🏗️ Proje Mimarisi

![KrakenD API Gateway Architecture](https://github.com/Muhammederendemir/spring-boot-krakend-api-gateway/blob/main/image/krakenD-api-gateway.png)

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   KrakenD       │    │   User Service  │    │ Product Service │
│   API Gateway   │◄──►│   (Port: 8080)  │    │  (Port: 8081)   │
│   (Port: 8000)  │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📋 Teknolojiler

### Backend
- **Spring Boot 3.5.4** - Ana framework
- **Java 21** - Programlama dili
- **Spring Data JPA** - Veritabanı erişimi
- **H2 Database** - In-memory veritabanı
- **Maven** - Bağımlılık yönetimi

### API Gateway
- **KrakenD** - API Gateway ve proxy
- **Docker** - Konteynerizasyon

## 🚀 Servisler

### 1. User Service (Port: 8080)
Kullanıcı yönetimi için geliştirilmiş mikroservis.

**Entity:**
- `User`: id, username, email, password alanları

**API Endpoints:**
- `GET /api/users` - Tüm kullanıcıları listele
- `POST /api/users` - Yeni kullanıcı oluştur

**Veritabanı:** H2 (userdb)

### 2. Product Service (Port: 8081)
Ürün yönetimi için geliştirilmiş mikroservis.

**Entity:**
- `Product`: id, name, description, price, stock alanları

**API Endpoints:**
- `GET /api/products` - Tüm ürünleri listele
- `GET /api/products/{id}` - ID'ye göre ürün getir
- `POST /api/products` - Yeni ürün oluştur
- `PUT /api/products/{id}` - Ürün güncelle
- `DELETE /api/products/{id}` - Ürün sil

**Veritabanı:** H2 (productdb)

### 3. KrakenD API Gateway (Port: 8000)
Mikroservisleri birleştiren ve yöneten API Gateway.

**Özellikler:**
- **Routing**: Servisler arası yönlendirme
- **Aggregation**: Birden fazla servisten veri toplama
- **Rate Limiting**: API kullanım sınırlaması
- **Authentication**: JWT tabanlı kimlik doğrulama

**API Endpoints:**
- `GET /api/users` - User Service'e yönlendirme
- `GET /api/products` - Product Service'e yönlendirme
- `GET /api/dashboard` - Aggregated endpoint (rate limited)

## 📁 Proje Yapısı

```
spring-boot-krakend/
├── docker-compose.yml                 # Docker konteyner yapılandırması
├── krakend.json                      # Ana KrakenD konfigürasyonu
├── krakend-aggregation.json          # Aggregation örneği
├── krakend-ratelimitter.json         # Rate limiting örneği
├── krakend-router.json               # Basit routing örneği
├── spring-boot-user-service/         # User mikroservisi
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/meddx/springbootuserservice/
│       ├── controller/UserController.java
│       ├── entity/User.java
│       ├── repository/UserRepository.java
│       ├── service/UserService.java
│       └── SpringBootUserServiceApplication.java
└── spring-boot-product-service/      # Product mikroservisi
    ├── Dockerfile
    ├── pom.xml
    └── src/main/java/com/meddx/springbootproductservice/
        ├── controller/ProductController.java
        ├── entity/Product.java
        ├── repository/ProductRepository.java
        ├── service/ProductService.java
        └── SpringBootProductServiceApplication.java
```

## 🛠️ Kurulum ve Çalıştırma

### Gereksinimler
- Docker ve Docker Compose
- Java 21
- Maven

### 1. Docker ile Çalıştırma (Önerilen)

```bash
# Projeyi klonlayın
git clone <repository-url>
cd spring-boot-krakend

# Docker konteynerlerini başlatın
docker-compose up --build
```

### 2. Manuel Çalıştırma

```bash
# User Service'i başlatın
cd spring-boot-user-service
mvn spring-boot:run

# Yeni terminal açın ve Product Service'i başlatın
cd spring-boot-product-service
mvn spring-boot:run

# KrakenD'i başlatın (Docker ile)
docker run -p 8000:8000 -v $(pwd)/krakend.json:/etc/krakend/krakend.json devopsfaith/krakend
```

## 🌐 API Kullanımı

### User Service (Direkt Erişim)
```bash
# Tüm kullanıcıları listele
curl http://localhost:8080/api/users

# Yeni kullanıcı oluştur
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}'
```

### Product Service (Direkt Erişim)
```bash
# Tüm ürünleri listele
curl http://localhost:8081/api/products

# Yeni ürün oluştur
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","description":"Test Description","price":99.99,"stock":10}'
```

### KrakenD API Gateway
```bash
# User Service'e yönlendirme
curl http://localhost:8000/api/users

# Product Service'e yönlendirme
curl http://localhost:8000/api/products

# Aggregated dashboard (rate limited)
curl http://localhost:8000/api/dashboard
```

## 🔧 Konfigürasyon

### KrakenD Konfigürasyonları

1. **krakend.json**: Ana konfigürasyon (routing + aggregation + rate limiting + auth)
2. **krakend-aggregation.json**: Sadece aggregation örneği
3. **krakend-ratelimitter.json**: Rate limiting örneği
4. **krakend-router.json**: Basit routing örneği

### Rate Limiting
- `/api/dashboard` endpoint'i için: 5 istek/dakika
- JWT authentication gerektirir

### Authentication
- JWT tabanlı kimlik doğrulama
- Secret key: `my-krakend-secret`
- Query parameter: `access_token`

## 🗄️ Veritabanı

Her servis kendi H2 in-memory veritabanını kullanır:
- **User Service**: `jdbc:h2:mem:userdb`
- **Product Service**: `jdbc:h2:mem:productdb`

H2 Console erişimi:
- User Service: http://localhost:8080/h2-console
- Product Service: http://localhost:8081/h2-console

## 🧪 Test

### Unit Testler
```bash
# User Service testleri
cd spring-boot-user-service
mvn test

# Product Service testleri
cd spring-boot-product-service
mvn test
```

### API Testleri
```bash
# Health check
curl http://localhost:8000/__health

# Service endpoints
curl http://localhost:8000/api/users
curl http://localhost:8000/api/products
```

## 📊 Monitoring

### Loglar
```bash
# Docker loglarını görüntüle
docker-compose logs -f

# Belirli servisin loglarını görüntüle
docker-compose logs -f users-service
docker-compose logs -f products-service
docker-compose logs -f krakend
```

### Health Checks
- KrakenD: http://localhost:8000/__health
- User Service: http://localhost:8080/actuator/health
- Product Service: http://localhost:8081/actuator/health

## 🔒 Güvenlik

- JWT tabanlı kimlik doğrulama
- Rate limiting
- Input validation
- SQL injection koruması (JPA)

## 🚀 Geliştirme

### Yeni Endpoint Ekleme
1. İlgili serviste controller'a endpoint ekleyin
2. KrakenD konfigürasyonunu güncelleyin
3. Docker Compose'u yeniden başlatın

### Yeni Servis Ekleme
1. Spring Boot projesi oluşturun
2. Dockerfile ekleyin
3. docker-compose.yml'a servisi ekleyin
4. KrakenD konfigürasyonunu güncelleyin

## 📝 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

## 👥 Katkıda Bulunma

Bu projeye katkıda bulunmak istiyorsanız:

1. [Bu repository'yi fork edin](https://github.com/Muhammederendemir/spring-boot-krakend-api-gateway/fork)
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. [Pull Request oluşturun](https://github.com/Muhammederendemir/spring-boot-krakend-api-gateway/compare)

### Katkı Türleri
- 🐛 Bug düzeltmeleri
- ✨ Yeni özellikler
- 📚 Dokümantasyon iyileştirmeleri
- 🎨 UI/UX iyileştirmeleri
- ⚡ Performance optimizasyonları
- 🔒 Güvenlik iyileştirmeleri

## 📞 İletişim

Proje hakkında sorularınız için:
- [GitHub Issues](https://github.com/Muhammederendemir/spring-boot-krakend-api-gateway/issues) sayfasından issue açabilirsiniz
- [GitHub Discussions](https://github.com/Muhammederendemir/spring-boot-krakend-api-gateway/discussions) sayfasından tartışma başlatabilirsiniz

## 🌟 Star History

Bu projeyi beğendiyseniz ⭐ star vermeyi unutmayın!

[![Star History Chart](https://api.star-history.com/svg?repos=Muhammederendemir/spring-boot-krakend-api-gateway&type=Date)](https://star-history.com/#Muhammederendemir/spring-boot-krakend-api-gateway&Date)
