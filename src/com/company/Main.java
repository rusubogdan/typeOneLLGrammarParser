package com.company;

class Main {

    public static void main(String []args) {
        Resolver resolver = new Resolver();
        boolean accepted = resolver.resolve();

        System.out.println(accepted);

    }
}
