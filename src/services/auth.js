import api from "./api";

export const login = (username, password) => {
  return api.post("/auth/login", { username, password });
};

export const logout = () => {
  localStorage.removeItem("user");
};

export const getUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};
