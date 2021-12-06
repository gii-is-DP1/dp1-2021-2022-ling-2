module.exports = {
  purge: ["./src/**/*.{js,jsx,ts,tsx}", "./public/index.html"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    scale: {
      150: "1.5",
      200: "2",
      250: "2.5",
      300: "3",
      400: "4",
    },
    extend: {
      backgroundImage: {
        // TODO this should be in /public/ folder
        wood: "url('images/backgrounds/wood.png')",
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
