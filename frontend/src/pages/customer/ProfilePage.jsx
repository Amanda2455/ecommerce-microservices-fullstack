import React from 'react';
import UserProfile from '../../components/customer/UserProfile';
import { useAuth } from '../../context/AuthContext';
import { Navigate } from 'react-router-dom';

const ProfilePage = () => {
  const { isAuthenticated } = useAuth();

  // Redirect to login if not authenticated
  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: '/profile' }} />;
  }

  return (
    <div>
      <UserProfile />
    </div>
  );
};

export default ProfilePage;