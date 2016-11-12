QT += core
QT += gui
QT += network

CONFIG += c++11

TARGET = loader
CONFIG += console
CONFIG -= app_bundle

TEMPLATE = app

SOURCES += main.cpp \
    export.cpp \
    binary.cpp

HEADERS += \
    binary.h \
    export.h
