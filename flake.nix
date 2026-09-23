{
  description = "Element X Android development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    android-nixpkgs = {
      url = "github:tadfisher/android-nixpkgs/stable";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, android-nixpkgs }:
    let
      systems = [ "x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
      forAllSystems = nixpkgs.lib.genAttrs systems;
    in
    {
      devShells = forAllSystems (system:
        let
          pkgs = import nixpkgs { inherit system; };
          androidSdk = android-nixpkgs.sdk.${system} (sdk: with sdk; [
            build-tools-37-0-0
            cmdline-tools-latest
            platform-tools
            platforms-android-37-0
          ]);
        in
        {
          default = pkgs.mkShell {
            packages = [ androidSdk pkgs.jdk21 pkgs.git ];
            ANDROID_HOME = "${androidSdk}/share/android-sdk";
            ANDROID_SDK_ROOT = "${androidSdk}/share/android-sdk";
            JAVA_HOME = "${pkgs.jdk21}/lib/openjdk";
            NIXPKGS_ACCEPT_ANDROID_SDK_LICENSE = "1";
          };
        });
    };
}
