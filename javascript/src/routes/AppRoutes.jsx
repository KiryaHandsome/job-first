import React from 'react';
import {Route, Routes} from 'react-router-dom';
import PrivateRoute from './PrivateRoute';
import {LoginPage} from "../pages/login/LoginPage.jsx";
import {VacanciesPage} from "../pages/vacancy/VacanciesPage/VacanciesPage.jsx";
import VacancyDetailsPage from "../pages/vacancy/VacancyDetailsPage/VacancyDetailsPage.jsx";
import {ResumesPage} from "../pages/ResumesPage/ResumesPage.jsx";
import ResumeDetailsPage from "../pages/ResumeDetailsPage/ResumeDetailsPage.jsx";
import EmployerVacanciesPage from "../pages/vacancy/EmployerVacanciesPage/EmployerVacanciesPage.jsx";
import CompanyPage from "../pages/CompanyPage/CompanyPage.jsx";
import ProfilePage from "../pages/ProfilePage/ProfilePage.jsx";
import CreateVacancyPage from "../pages/vacancy/CreateVacancyPage/CreateVacancyPage.jsx";
import EmployerVacancyDetailsPage from "../pages/vacancy/EmployerVacancyDetailsPage/EmployerVacancyDetailsPage.jsx";
import EditVacancyPage from "../pages/vacancy/EditVacancyPage/EditVacancyPage.jsx";
import RegisterPage from "../pages/auth/RegisterPage/RegisterPage.jsx";


const AppRoutes = ({setIsAuthenticated}) => {
    return (
        <Routes>
            {/* Public routes */}
            <Route path="/login" element={<LoginPage setIsAuthenticated={setIsAuthenticated}/>}/>
            <Route path="/register" element={<RegisterPage/>}/>
            <Route path="/vacancies" element={<VacanciesPage/>}/>
            <Route path="/vacancies/:id" element={<VacancyDetailsPage/>}/>


            {/* Protected routes */}
            <Route element={<PrivateRoute/>}>
                <Route path="/resumes" element={<ResumesPage/>}/>
                <Route path="/resumes/:id" element={<ResumeDetailsPage/>}/>
                <Route path="/employer-vacancies" element={<EmployerVacanciesPage/>}/>
                <Route path="/employer-vacancies/:id" element={<EmployerVacancyDetailsPage/>}/>
                <Route path="/employer-vacancies/:id/edit" element={<EditVacancyPage/>}/>
                <Route path="/create-vacancy" element={<CreateVacancyPage/>}/>
                <Route path="/company" element={<CompanyPage/>}/>
                <Route path="/profile" element={<ProfilePage/>}/>
            </Route>
        </Routes>
    );
};

export default AppRoutes; 