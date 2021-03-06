module.exports = {
  purge: ["./src/**/*.{js,jsx,ts,tsx}", "./public/index.html"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    scale: {
      50: "0.5",
      110: "1.1",
      150: "1.5",
      200: "2",
      250: "2.5",
      300: "3",
      350: "3.5",
      400: "4",
      500: "5",
    },
    extend: {
      backgroundImage: {
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
