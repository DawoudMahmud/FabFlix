import React from "react";
import {NavLink} from "react-router-dom";
import styled from "styled-components";

const StyledNav = styled.nav`
  display: flex;

  width: calc(100vw - 10px);
  height: 50px;
  padding: 5px;

  background-color: gray;
`;
const StyledUserLink = styled(NavLink)`
  padding: 30px;
  font-size: 15px;
  color: #000;
  justify-content: center;
  text-align: center;
  text-decoration: none;
  &:hover {
        background-color: #eef;
        border: 1px solid #aaf;
      }
`;

const StyledHomeLink = styled(NavLink)`
  padding: 10px;
  font-size: 30px;
  color: #000;
  justify-content: left;
  text-decoration: none;
  &:hover {
        background-color: dark gray;
        border: 1px solid lightblue;
      }
`;

/**
 * To be able to navigate around the website we have these NavLink's (Notice
 * that they are "styled" NavLink's that are now named StyledNavLink)
 * <br>
 * Whenever you add a NavLink here make sure to add a corresponding Route in
 * the Content Component
 * <br>
 * You can add as many Link as you would like here to allow for better navigation
 * <br>
 * Below we have two Links:
 * <li>Home - A link that will change the url of the page to "/"
 * <li>Login - A link that will change the url of the page to "/login"
 */
const NavBar = () => {
    return (
        <StyledNav>
            <StyledHomeLink to="/">
                FabFlix
            </StyledHomeLink>
            <StyledUserLink to="/register">
                Register
            </StyledUserLink>
            <StyledUserLink to="/login">
                Login
            </StyledUserLink>
            <StyledUserLink to="/movie/search">
                Search
            </StyledUserLink>
        </StyledNav>
    );
}

export default NavBar;
