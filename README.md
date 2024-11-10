# java-convenience-store-precourse

### 개요
고객이 상품과 수량을 선택하고, 멤버십 할인을 포함한 프로모션 혜택을 적용하여 최종 결제 금액을 계산합니다.
결제 시스템은 프로모션 및 멤버십 할인, 재고 관리 등을 고려하며, 영수증을 출력하여 최종 결제 내용을 안내합니다.

## 주요 기능
- 상품 관리: 파일을 통해 상품 및 프로모션 정보를 불러오고, 재고와 가격을 관리합니다.
- 프로모션 적용: 특정 상품에 대해 N+1 형태의 프로모션 혜택을 제공하며, 프로모션 재고 소진 여부를 확인하고 적용합니다.
- 멤버십 할인: 멤버십 회원에게 프로모션 미적용 금액의 30%를 최대 8,000원까지 할인합니다.
- 영수증 출력: 고객의 구매 내역, 할인 금액, 최종 결제 금액을 포함한 영수증을 출력합니다.
- 입력 유효성 검사: 잘못된 입력 시 오류 메시지를 출력하고, 올바른 입력을 받을 때까지 재시도합니다.


## 📦 패키지 구조
src/
├── controller/
│   └── ConvenienceStoreController
├── domain/
│   ├── order/
│   │   ├── Order
│   │   └── Orders
│   ├── product/
│   │   ├── MemberShip
│   │   ├── NoPromotionProducts
│   │   ├── Product
│   │   ├── Promotion
│   │   └── PromotionProducts
│   ├── purchasedProduct/
│   │   └── PurchasedProduct
│   └── receipt/
│       └── Receipt
├── service/
│   ├── FileService
│   ├── PaymentService
│   ├── ProductService
│   └── PromotionService
├── util/
│   ├── ErrorMessage
│   ├── FilePath
│   ├── InputValidator
│   ├── Parser
│   ├── PaymentValidator
│   └── YesNoOption
└── view/
├── InputView
├── OutputView
└── Application

### 구조 설명

- controller: ConvenienceStoreController 클래스가 있으며, 애플리케이션의 주요 흐름을 제어합니다. 사용자의 입력과 전체 쇼핑 흐름을 관리합니다.

- domain: 주요 도메인 객체들을 포함하고 있습니다.
    - order: Order와 Orders 클래스를 통해 주문 관련 정보를 관리합니다.
    - product: 상품 관련 클래스(Product, Promotion, PromotionProducts, NoPromotionProducts, MemberShip)가 있으며, 각각 상품 정보, 프로모션 정보, 프로모션이 있는 상품과 없는 상품을 구분하여 저장합니다.
    - purchasedProduct: PurchasedProduct 클래스를 통해 실제로 구매된 상품의 정보를 저장합니다.
    - receipt: Receipt 클래스는 최종 영수증을 생성하고 관리합니다.
  
- service: 비즈니스 로직을 수행하는 서비스 클래스들이 위치합니다.
    - FileService: 파일 입출력을 통해 상품과 프로모션 데이터를 로드하고 업데이트하는 기능을 수행합니다.
    - PaymentService: 결제 로직을 처리하고 구매 목록을 반환합니다.
    - ProductService: 상품 및 프로모션 관련 정보를 관리합니다.
    - PromotionService: 프로모션 적용 로직을 처리합니다.


- util: 유틸리티 클래스들을 포함하며, 공통 기능을 제공합니다.
    - ErrorMessage: 에러 메시지를 관리합니다.
    - FilePath: 파일 경로를 관리합니다.
    - InputValidator: 입력 값을 검증하는 유틸리티 클래스입니다.
    - Parser: 문자열 파싱을 수행합니다.
    - PaymentValidator: 결제 시 유효성 검사를 수행합니다.
    - YesNoOption: 예/아니오 옵션을 관리합니다.
  
- view: 입출력 처리를 담당하는 클래스들이 포함되어 있습니다.
    - InputView: 사용자로부터 입력을 받습니다.
    - OutputView: 화면에 출력을 담당합니다.