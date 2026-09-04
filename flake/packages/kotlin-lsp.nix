{
  autoPatchelfHook,
  jetbrains,
  makeWrapper,
  stdenv,
  stdenvNoCC,
}:

stdenvNoCC.mkDerivation (finalAttrs: {
  pname = "kotlin-lsp";
  version = "262.9593.0";

  src = fetchTarball {
    url = "https://download-cdn.jetbrains.com/language-server/kotlin-server/${finalAttrs.version}/kotlin-server-${finalAttrs.version}.tar.gz";
    sha256 = "1b0jvrm1qy1f7f8085b93k0ma5x6iyh39sp2yl5y4sv046xyza79";
  };

  nativeBuildInputs = [
    makeWrapper
    autoPatchelfHook
  ];

  buildInputs = [
    stdenv.cc.cc.lib
  ];

  installPhase = ''
    runHook preInstall

    mkdir -p $out/{bin,share}
    cp -r bin lib license modules plugins product-info.json $out/share
    ln -sT "${jetbrains.jdk-no-jcef}"/lib/openjdk $out/share/jbr

    makeWrapper $out/share/bin/intellij-server $out/bin/intellij-server

    runHook postInstall
  '';
})
