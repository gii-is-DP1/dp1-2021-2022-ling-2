module.exports = {
  purge: ["./src/**/*.{js,jsx,ts,tsx}", "./public/index.html"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      backgroundImage: {
        // TODO this should be in /public/ folder
        wood: "url('images/backgrounds/wood.png')",
        green: "url('images/backgrounds/green.png')",
        felt: "url('images/backgrounds/felt.png')",
        fabric: "url('images/backgrounds/fabric.png')",
        leather: "url('images/backgrounds/leather.png')",
        gold: "url('images/backgrounds/gold.png')",
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
};
