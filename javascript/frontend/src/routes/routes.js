import { LoginPage } from '../pages/login/LoginPage';
import { HomePage } from '../pages/HomePage';
import { VacanciesPage } from '../pages/VacanciesPage/VacanciesPage';
import ProfilePage from '../pages/ProfilePage/ProfilePage';
import { ResumesPage } from '../pages/ResumesPage/ResumesPage';
import  ResumeDetailsPage  from '../pages/ResumeDetailsPage/ResumeDetailsPage';
import VacancyDetailsPage from '../pages/VacancyDetailsPage/VacancyDetailsPage';

export const routes = [
    {
        path: '/login',
        element: <LoginPage />,
        public: true
    },
    // {
    //     path: '/register',
    //     element: <RegisterPage />,
    //     public: true
    // },
    {
        path: '/',
        element: <HomePage />,
        public: false,
        layout: true
    },
    {
        path: '/vacancies',
        element: <VacanciesPage />,
        public: false,
        layout: true
    },
    {
        path: '/vacancies/:id',
        element: <VacancyDetailsPage />,
        public: true,
        layout: true
    },
    {
        path: '/resumes',
        element: <ResumesPage />,
        public: false,
        layout: true
    },
    {
        path: '/resumes/:id',
        element: <ResumeDetailsPage />,
        public: false,
        layout: true
    },
    {
        path: '/profile',
        element: <ProfilePage />,
        public: false,
        layout: true
    }
];
