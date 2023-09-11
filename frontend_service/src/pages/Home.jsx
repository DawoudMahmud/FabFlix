import React, {useEffect} from "react";
import styled from "styled-components";
import {useUser} from "../hook/User";
import {useNavigate} from "react-router-dom";

const StyledDiv = styled.div` 
`

const Styledp = styled.p`
    text-align: center;
    padding-top: 5px;
`

const StyledRerouteButton = styled.button`
  border: none;
  font-size: 17px;
  color: blue;
  justify-content: left;
  text-decoration: none;
  background: pink;
  &:hover {
        background-color: none;
        border: 1px solid #aaf;
      }
`

const Home = () => {
    const {accessToken} = useUser();
    const navigate = useNavigate();


    useEffect(() => {
        if(accessToken == null){
            navigate("/login");
        }
    })

    const moveToSearch = () => {
        navigate("/movie/search");
    };

    return (
        <div>
            <h1>Welcome to FabFlix</h1>
            <br></br>
            <Styledp> Search for a movie <StyledRerouteButton onClick={moveToSearch}> here! </StyledRerouteButton> </Styledp>


        </div>
    );
}

export default Home;
