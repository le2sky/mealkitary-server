<div align="center">
  <br>
  <h1>온라인 밀키트 예약 서비스, 밀키터리 🍣</h1>
  <strong>서버 애플리케이션 저장소</strong>
</div>
<br>

<p align="center">
    <img src="https://img.shields.io/github/commit-activity/w/le2sky/mealkitary-server" alt="GitHub commit activity">
    <a href ="https://opensource.org/licenses/Apache-2.0">
        <img src="https://img.shields.io/badge/License-Apache%202.0-red.svg"/>
    </a>
    <a href="https://codecov.io/gh/le2sky/mealkitary-server" >
        <img src="https://codecov.io/gh/le2sky/mealkitary-server/branch/main/graph/badge.svg?token=MRR5C1OFUL"/>
    </a>
    <a href="https://github.com/le2sky/mealkitary-server/.git/workflows/mealkitary-main-cd.yml">
        <img src="https://github.com/le2sky/mealkitary-server/actions/workflows/mealkitary-main-cd.yml/badge.svg" alt="Build Status">
    </a>
</p>

## 프로젝트 소개

밀키트 자영업자인 지인의 문제로부터 시작된 프로젝트입니다. 밀키트의 소비기한은 3일로 매우 짧습니다.
또한 소비자의 니즈를 예측해서 진열해야 하는 문제와 시너지를 발휘해 소비기한이 지난 밀키트가 쌓여가는 문제점이 있었습니다. 저희 팀은 예약이라는 개념을 이용해 소비자의 니즈를 예측하는 어려움을 해소하고자 **온라인
밀키트 예약 서비스**를 만들기로 결정 했습니다.

## 목차

- [프로젝트 소개](#프로젝트-소개)
- [목차](#목차)
- [개발 가이드](#개발-가이드)
- [구현 설명](#구현-설명)
    - [모듈 구조](#모듈-구조)
    - [프레임워크 및 라이브러리](#프레임워크-및-라이브러리)

## 개발 가이드

[개발 가이드 보러가기](https://github.com/le2sky/mealkitary-server/blob/main/docs/tech.md)

## 구현 설명

모듈 구조와 사용한 기술에 대한 간략한 설명입니다.

### 모듈 구조

각 모듈의 내부 구조는 포트와 어댑터 패턴을 따릅니다.

- `mealkitary-api`
- `mealkitary-application`
- `mealkitary-domain`
- `mealkitary-infrastructure`

### 프레임워크 및 라이브러리

- 개발 : `jvm 11`, `kotlin 1.6.x`, `springboot 2.7.x`
- 테스트 : `kotest 4.4.x`, `mockk 1.12.x`, `jacoco 0.8.x`, `springmockk 3.0.x`

[⬆ 위로 올라가기](#목차)
